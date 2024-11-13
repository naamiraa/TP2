public class debugSofiKetemuJoki {
    else if (query.equals("M")) {
        String arah = in.next();  // Mendapatkan arah perpindahan: 'L' atau 'R'
        Tim timSekarang = LLTim.getSofitaSekarang();  // Ambil tim yang diawasi Sofita

        // Memproses perintah M
        if (timSekarang != null) {
            // out.println("Sofita sekarang mengawasi Tim " + timSekarang.idTim);
            // out.println("Peserta di Tim " + timSekarang.idTim + " dan poin mereka:");
            // timSekarang.treePeserta.printPeserta();
    
            Tim timDenganPoinTerendah = LLTim.getTimDenganPoinTerendah();
            // if (timDenganPoinTerendah != null) {
            //     out.println("Joki berada di Tim " + LLTim.jokiSaiki.idTim);
            // }
    
            // Perpindahan Sofita ke kiri atau kanan
            if (arah.equals("L")) {
                // Pindah ke kiri
                if (timSekarang == LLTim.getFirst()) {
                    timSekarang = LLTim.getLast();  // Pindah ke tim terakhir jika Sofita di tim pertama
                    timSekarang = LLTim.getLast();
                } else {
                    timSekarang = timSekarang.prev;  // Pindah ke tim sebelah kiri
                    timSekarang = timSekarang.prev;
                }
            } else if (arah.equals("R")) {
                // Pindah ke kanan
                if (timSekarang == LLTim.getLast()) {
                    timSekarang = LLTim.getFirst();  // Pindah ke tim pertama jika Sofita di tim terakhir
                    timSekarang = LLTim.getFirst();
                } else {
                    timSekarang = timSekarang.next;  // Pindah ke tim sebelah kanan
                    timSekarang = timSekarang.next;
                }
            }
    
            // LLTim.setsofitaSekarang(timSekarang);
    
            // out.println("Posisi Sofita sekarang pada Tim " + timSekarang.idTim);
            // out.println("Peserta di Tim " + timSekarang.idTim + " dan poin mereka:");
            // timSekarang.treePeserta.printPeserta();
    
            if (timSekarang == LLTim.jokiSaiki) {
                timSekarang.sofiKetemuJoki++;  // Tambahkan hitungan Sofita ketemu joki
    
                // Terapkan aturan sesuai jumlah pertemuan dengan joki
                if (timSekarang.sofiKetemuJoki == 1) {
                    // out.println("jml peserta sebelum remove: " + timSekarang.jumlahPeserta);
                    timSekarang.treePeserta.removeTopThree();  // Hapus tiga peserta dengan poin terbesar
                    out.println("Ketahuan 1x: Tiga peserta dengan poin terbesar dikeluarkan dari tim " + timSekarang.idTim);
    
                    // Update jumlah peserta setelah penghapusan
                    timSekarang.jumlahPeserta -= 3;  // Mengurangi peserta yang dihapus
    
                    // out.println("Jumlah peserta setelah remove: " + timSekarang.jumlahPeserta);
    
                    // Jika jumlah peserta tim sekarang kurang dari 6, hapus tim tersebut dan pindahkan Sofita
                    if (timSekarang.sofiKetemuJoki == 1) {
                        // LLTim.remove(timSekarang);  // Hapus tim dari daftar
                        // out.println("Tim " + timSekarang.idTim + " dieliminasi.");
                        // Pindahkan Sofita ke tim dengan poin terbesar
                        Tim timBaruUntukSofita = LLTim.getTimDenganPoinTerbesar();
                        LLTim.setsofitaSekarang(timBaruUntukSofita);
                        out.println("Sofita berpindah ke tim dengan poin terbesar, Tim " + timBaruUntukSofita.idTim);
                        // out.println("Peserta di Tim " + timBaruUntukSofita.idTim + " dan poin mereka:");
                        // timBaruUntukSofita.treePeserta.printPeserta();
                    }
                } else if (timSekarang.sofiKetemuJoki == 2) {
                    timSekarang.treePeserta.setAllPointsToOne();  // Ubah poin semua peserta jadi satu
                    out.println("Ketahuan 2x: Semua peserta di tim " + timSekarang.idTim + " diubah poinnya menjadi satu.");
                } else if (timSekarang.sofiKetemuJoki == 3) {
                    LLTim.remove(timSekarang);  // Hapus tim dari daftar
                    out.println("Ketahuan 3x: Tim " + timSekarang.idTim + " dieliminasi.");
                    timSekarang = LLTim.getTimDenganPoinTerbesar();
                    LLTim.setsofitaSekarang(timSekarang);  // Pindahkan Sofita ke tim dengan poin terbesar
                    out.println("Sofita berpindah ke tim dengan poin terbesar, Tim " + timSekarang.idTim);
                    // out.println("Peserta di Tim " + timSekarang.idTim + " dan poin mereka:");
                    // timSekarang.treePeserta.printPeserta();
                }
    
                // Pindahkan joki ke tim dengan poin terendah (kedua terendah jika tim saat ini)
                Tim timBaruUntukJoki = LLTim.getTimDenganPoinTerendah();
                if (timBaruUntukJoki == timSekarang) {
                    timBaruUntukJoki = LLTim.getTimDenganPoinTerendahKedua();
                }
                LLTim.jokiSaiki = timBaruUntukJoki;  // Pindahkan joki ke tim baru
                // out.println("Joki berpindah ke Tim " + timBaruUntukJoki.idTim);
            }
            LLTim.setsofitaSekarang(timSekarang);  // Set tim yang diawasi Sofita
            out.println(timSekarang.idTim);  // Cetak ID tim yang diawasi Sofita
        } else {
            out.println(-1);
        }
    }
}
