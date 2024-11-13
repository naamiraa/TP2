/*
SOURCE AND REFERENCE

Github kak Eugenius Mario : https://github.com/eugeniusms/SDA-2022
Github kak Hilmi Atha: https://github.com/hilmiatha/SDA23-hilmoy/tree/main/TP02

*/


import java.io.*;
import java.util.*;


public class TP2 {
    private static InputReader in;
    private static PrintWriter out;

    static int idTim = 1;
    static int idPeserta = 1;
    static CircularDoublyLL<Tim> LLTim = new CircularDoublyLL<>();


    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int banyakTim = in.nextInt();
        for(int i = 0 ; i < banyakTim ; i++){
            int jumlahPeserta = in.nextInt();
            AVLTree treePeserta = new AVLTree();
            int id = idTim;
            idTim++;
            Tim tim = new Tim(treePeserta, 0, id);
            tim.jumlahPeserta = jumlahPeserta;
            LLTim.addLast(tim);
        }

        Tim current = LLTim.header;

        for(int i = 0; i < LLTim.size; i++){
            current = current.next;
            long sumScore = 0;
            for(long z = 0; z < current.jumlahPeserta ; z++){
                int poin = in.nextInt();
                int id = idPeserta;
                id++;
                sumScore += poin;
                // Create a Peserta object and insert it into the tree
                Peserta peserta = new Peserta(id, poin);
                current.treePeserta.root = current.treePeserta.insert(current.treePeserta.root, peserta);
            }
            current.sumScore = sumScore;
        }

        LLTim.setsofitaSekarang(LLTim.header.next);
        LLTim.iniPenjokiAwal();

        int q = in.nextInt();
        for (int i = 0; i < q ; i++){
            String query = in.next();
            if (query.equals("A")) {
                int jumlahPesertaBaru = in.nextInt();
                Tim timASekarang = LLTim.getSofitaSekarang();  // Ambil tim yang diawasi Sofita
            
                // Memproses perintah A
                if (timASekarang != null) {
                    for (int j = 0; j < jumlahPesertaBaru; j++) {
                        int id = idPeserta++;
                        int poinBaru = 3; // Assuming 3 as the new participant's poin

                        // Create a new Peserta object and insert it
                        Peserta pesertaBaru = new Peserta(id, poinBaru);
                        timASekarang.treePeserta.root = timASekarang.treePeserta.insert(timASekarang.treePeserta.root, pesertaBaru);
                    }
                    timASekarang.jumlahPeserta += jumlahPesertaBaru;
                    out.println(timASekarang.jumlahPeserta);
                } else {
                    out.println(-1);
                }
            }

            else if (query.equals("G")) {
                String posisi = in.next();  // Mendapatkan arah pembentukan tim baru: 'L' atau 'R'
                Tim timSofita = LLTim.getSofitaSekarang();  // Ambil tim yang sedang diawasi oleh Sofita
                int idTimBaru = idTim++;  // Generate ID baru untuk tim yang akan dibuat
                AVLTree treePesertaBaru = new AVLTree();  // Buat AVLTree baru untuk tim
            
                // Menambahkan 7 peserta dengan poin awal 1 ke tim baru
                for (int j = 0; j < 7; j++) {
                    int idPesertaBaru = idPeserta++;  // ID untuk setiap peserta baru
                    Peserta pesertaBaru = new Peserta(idPesertaBaru, 1);  // Setiap peserta dengan poin awal 1
                    treePesertaBaru.root = treePesertaBaru.insert(treePesertaBaru.root, pesertaBaru);  // Insert ke AVLTree
                }
            
                // Buat tim baru dengan AVLTree peserta dan jumlah peserta awal 7
                Tim timBaru = new Tim(treePesertaBaru, 7, idTimBaru);
            
                // Tentukan posisi penyisipan tim baru berdasarkan arah ('L' atau 'R')
                if (posisi.equals("L")) {
                    LLTim.addBefore(timSofita, timBaru);  // Menambahkan tim baru di sebelah kiri tim yang diawasi Sofita
                } else if (posisi.equals("R")) {
                    LLTim.addAfter(timSofita, timBaru);  // Menambahkan tim baru di sebelah kanan tim yang diawasi Sofita
                }
            
                out.println(timBaru.idTim);  // Cetak ID tim baru
            }
            

            // else if (query.equals("M")) {
            //     String arah = in.next();  // Mendapatkan arah perpindahan: 'L' atau 'R'
            //     Tim timSekarang = LLTim.getSofitaSekarang();  // Ambil tim yang diawasi Sofita
            //     int idTimSofitaSebelumnya = timSekarang.idTim;  // Simpan ID tim sebelumnya
            //     boolean sudahOutput = false;  // Variabel untuk mengontrol apakah output sudah dicetak
            
            //     if (timSekarang != null) {
            //         // Perpindahan Sofita ke kiri atau kanan
            //         if (arah.equals("L")) {
            //             if (timSekarang == LLTim.getFirst()) {
            //                 timSekarang = LLTim.getLast();
            //             } else {
            //                 timSekarang = timSekarang.prev;
            //             }
            //         } else if (arah.equals("R")) {
            //             if (timSekarang == LLTim.getLast()) {
            //                 timSekarang = LLTim.getFirst();
            //             } else {
            //                 timSekarang = timSekarang.next;
            //             }
            //         }
            
            //         // Set Sofita pada tim yang baru
            //         LLTim.setsofitaSekarang(timSekarang);
            
            //         // Hanya cetak ID tim jika benar-benar ada perubahan tim Sofita
            //         if (idTimSofitaSebelumnya != timSekarang.idTim) {
            //             out.println(timSekarang.idTim);  // Cetak ID tim baru jika Sofita pindah
            //             sudahOutput = true;  // Tandai bahwa output telah dicetak
            //         }
            
            //         // Jika Sofita bertemu dengan Joki dan belum ada output yang dicetak
            //         if (timSekarang == LLTim.jokiSaiki && !sudahOutput) {
            //             timSekarang.sofiKetemuJoki++;  // Tambah hitungan Sofita ketemu joki
            
            //             if (timSekarang.sofiKetemuJoki == 1) {
            //                 timSekarang.treePeserta.removeTopThree();  // Hapus tiga peserta dengan poin terbesar
            //                 timSekarang.jumlahPeserta -= 3;  // Mengurangi peserta yang dihapus
            
            //                 // Pindahkan Sofita ke tim dengan poin terbesar
            //                 Tim timBaruUntukSofita = LLTim.getTimDenganPoinTerbesar();
            //                 LLTim.setsofitaSekarang(timBaruUntukSofita);
            //                 out.println(timBaruUntukSofita.idTim);  // Cetak ID tim baru setelah pertemuan Joki
            //                 sudahOutput = true;  // Tandai bahwa output telah dicetak
            
            //             } else if (timSekarang.sofiKetemuJoki == 2) {
            //                 timSekarang.treePeserta.setAllPointsToOne();  // Set poin semua peserta menjadi 1
            //                 out.println("Ketahuan 2x: Semua peserta di tim " + timSekarang.idTim + " diubah poinnya menjadi satu.");
            //                 sudahOutput = true;  // Tandai bahwa output telah dicetak
            //             } else if (timSekarang.sofiKetemuJoki == 3) {
            //                 LLTim.remove(timSekarang);  // Hapus tim dari daftar
            //                 out.println("Ketahuan 3x: Tim " + timSekarang.idTim + " dieliminasi.");
            //                 timSekarang = LLTim.getTimDenganPoinTerbesar();
            //                 LLTim.setsofitaSekarang(timSekarang);  // Pindahkan Sofita ke tim dengan poin terbesar
            //                 out.println(timSekarang.idTim);  // Cetak ID tim setelah eliminasi
            //                 sudahOutput = true;  // Tandai bahwa output telah dicetak
            //             }
            
            //             // Pindahkan joki ke tim dengan poin terendah
            //             Tim timBaruUntukJoki = LLTim.getTimDenganPoinTerendah();
            //             if (timBaruUntukJoki == timSekarang) {
            //                 timBaruUntukJoki = LLTim.getTimDenganPoinTerendahKedua();
            //             }
            //             LLTim.jokiSaiki = timBaruUntukJoki;  // Pindahkan joki ke tim baru
            //         }
                    
            //         // Pastikan tidak ada output lebih dari satu kali pada setiap perintah M
            //     }
            // }
            

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
            

            else if (query.equals("J")) {
                String arah = in.next();
                Tim nextTim = null;
            
                if (arah.equals("L")) {
                    // Gerakkan penjoki ke kiri
                    nextTim = LLTim.gerakKiriPenjoki();
                } else if (arah.equals("R")) {
                    // Gerakkan penjoki ke kanan
                    nextTim = LLTim.gerakKananPenjoki();
                }
            
                if (nextTim != null) {
                    if (nextTim == LLTim.getSofitaSekarang()) {
                        // Jika penjoki tiba di tim yang diawasi oleh Sofita, cetak -1
                        // out.println(-1);
            
                        // Panggil metode iniPenjokiAwal untuk memindahkan penjoki ke tim dengan skor terendah atau kedua terendah
                        LLTim.iniPenjokiAwal();
            
                        // Cetak ID tim baru setelah masuk ke tim dengan poin terendah/terendah kedua
                        out.println(LLTim.jokiSaiki.idTim);
                    } else {
                        // Update posisi penjoki dan cetak ID tim
                        LLTim.setPenjokiNow(nextTim);
                        out.println(LLTim.jokiSaiki.idTim);
                    }
                }
            }

            else if (query.equals("U")) {
                // Ambil tim yang diawasi Sofita
                Tim timSofita = LLTim.getSofitaSekarang();
                
                // Jika tim yang diawasi Sofita ada, hitung jumlah skor unik
                if (timSofita != null) {
                    Node root = timSofita.treePeserta.root;
                    long jumlahSkorUnik = 0;
                    
                    // Melakukan in-order traversal untuk menghitung skor unik
                    if (root != null) {
                        long[] skorPeserta = new long[1000]; // Array dengan ukuran tetap untuk menyimpan skor
                        int[] index = {0}; // Gunakan array 1-elemen untuk menjaga index sebagai counter
                        AVLTree.untukCekPoinUnik(root, skorPeserta, index);
            
                        // // Manual sort (Bubble Sort) pada array skorPeserta
                        // for (int k = 0; k < index[0] - 1; k++) {
                        //     for (int j = 0; j < index[0] - i - 1; j++) {
                        //         if (skorPeserta[j] > skorPeserta[j + 1]) {
                        //             long temp = skorPeserta[j];
                        //             skorPeserta[j] = skorPeserta[j + 1];
                        //             skorPeserta[j + 1] = temp;
                        //         }
                        //     }
                        // }
            
                        // Menghitung jumlah skor unik dalam array skorPeserta
                        for (int j = 0; j < index[0]; j++) {
                            if (j == 0 || skorPeserta[j] != skorPeserta[j - 1]) {
                                jumlahSkorUnik++;
                            }
                        }
                    }
                    
                    // Cetak hasil jumlah skor unik
                    out.println(jumlahSkorUnik);
                } else {
                    out.println(-1); // Jika tidak ada tim yang diawasi Sofita, cetak -1
                }
            }
            
            
            
            
           
            
        }

        out.close();
    }

    // taken from https://www.programiz.com/dsa/avl-tree
    // a method to print the contents of a Tree data structure in a readable
    // format. it is encouraged to use this method for debugging purposes.
    // to use, simply copy and paste this line of code:
    // printTree(tree.root, "", true);
    static void printTree(Node currPtr, String indent, boolean last) {
        if (currPtr != null) {
            out.print(indent);
            if (last) {
                out.print("R----");
                indent += "   ";
            } else {
                out.print("L----");
                indent += "|  ";
            }
            out.println(currPtr.key);
            printTree(currPtr.left, indent, false);
            printTree(currPtr.right, indent, true);
        }
    }


    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    private static class InputReader {

        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public char nextChar() {
            return next().charAt(0);
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    static class Tim {
        Tim prev, next;
        AVLTree treePeserta;
        int idTim;
        int jumlahPeserta;
        int totalPoinTim;
        int sofiKetemuJoki;
        long sumScore;

        public Tim(AVLTree treePeserta, long sumScore, int idTim) {
            this.treePeserta = treePeserta;
            this.idTim = idTim;
            this.sumScore = sumScore;
            this.treePeserta = treePeserta;
        }
        
    }

    static class Peserta{
        int idPeserta;
        int poinPeserta;
        int jumlahMatch;

        public Peserta(int idPeserta, int poinPeserta) {
            this.idPeserta = idPeserta;
            this.poinPeserta = poinPeserta;
            this.jumlahMatch = 0;
        }
        
    
    }

    // Class DaftarTim digunakan untuk menyimpan semua Tim
    static class CircularDoublyLL<E> {
        int size; // Jumlah Tim
        Tim header, footer; // null node for easier add and remove
        Tim sofitaSekarang; // untuk menyimpan lokasi Sofita
        Tim jokiSaiki;

        // construct empty list
        CircularDoublyLL() {
            this.size = 0;
            this.header = new Tim(null, 0, 0);
            this.footer = new Tim(null, 0, 0);
        }

        /*BUAT DEBUG */
        public Tim getTimDenganPoinTerendah() {
            Tim timDenganPoinTerendah = null;
            int poinTerendah = Integer.MAX_VALUE;
        
            // Iterate over the teams in the CircularDoublyLL
            Tim currentTim = this.header;  // Assuming 'first' is the first team in the list
            while (currentTim != null) {
                // Check if the current team's total points are lower than the lowest found so far
                if (currentTim.totalPoinTim < poinTerendah) {
                    timDenganPoinTerendah = currentTim;
                    poinTerendah = currentTim.totalPoinTim;
                }
                currentTim = currentTim.next;  // Move to the next team
            }
        
            return timDenganPoinTerendah;  // Return the team with the lowest points
        }
        


        Tim getTimDenganPoinTerbesar() {
            Tim timTerbesar = header.next;
            if (timTerbesar == footer || timTerbesar == null) {
                return null;  // If there's only one team or the list is empty, return null
            }
            
            Tim current = timTerbesar.next;  // Start from the second node
            
            while (current != header) {
                // System.out.println("Checking team: " + current);  // Debug log
                if (current.totalPoinTim > timTerbesar.totalPoinTim) {
                    timTerbesar = current;  // Update timTerbesar if a team with larger points is found
                }
                current = current.next;  // Move to the next node
                if (current == null) {
                    break;  // If current is null, break the loop
                }
            }
            
            return timTerbesar;
            
            
        }
        


        public Tim getTimDenganPoinTerendahKedua() {
            Tim lowest = null;
            Tim secondLowest = null;
            Tim current = header;
        
            do {
                if (lowest == null || current.totalPoinTim < lowest.totalPoinTim) {
                    secondLowest = lowest;
                    lowest = current;
                } else if (secondLowest == null || current.totalPoinTim < secondLowest.totalPoinTim) {
                    secondLowest = current;
                }
                current = current.next;
            } while (current != header);
        
            return secondLowest;
        }
        


        Tim getLast(){
            return footer.prev;
        }

        Tim getFirst() {
            return header.next;
        }

        // Method digunakan untuk menambahkan node baru di akhir linkedlist
        void addLast(Tim Tim) {
            if (this.size == 0) { // empty
                footer.prev = Tim;
                Tim.next = footer;
                header.next = Tim;
                Tim.prev = header;

            } else { // is exist
                footer.prev.next = Tim;
                Tim.prev = footer.prev;
                Tim.next = footer;
                footer.prev = Tim;
            }

            this.size += 1;
        }


        // query G
        // Menambahkan node sebelum node yang ditentukan
        void addBefore(Tim newNode, Tim existingNode) {
            if (existingNode == header.next) { // Jika existingNode adalah node pertama
                newNode.next = existingNode;
                newNode.prev = header;
                header.next = newNode;
                existingNode.prev = newNode;
            } else if (existingNode.prev != null) { // Untuk memastikan tidak null
                newNode.prev = existingNode.prev;
                newNode.next = existingNode;
                existingNode.prev.next = newNode;
                existingNode.prev = newNode;
            }
            size++;
        }
        
        

        // Menambahkan node setelah node yang ditentukan
        void addAfter(Tim newNode, Tim existingNode) {
            if (existingNode == footer.prev) { // Jika existingNode adalah node terakhir
                footer.prev = newNode;
                newNode.next = footer;
            } else {
                newNode.next = existingNode.next;
                existingNode.next.prev = newNode;
            }
            newNode.prev = existingNode;
            existingNode.next = newNode;
            size++;
        }




        // Method digunakan untuk remove node Tim dari linkedlist
        Tim remove(Tim Tim) {
            if (this.size == 0) { // empty
                // do nothing
                throw new NullPointerException("LinkedList Size is 0");
            } else if (this.size == 1) { // tidak ada elemen kedua
                header.next = footer;
                footer.prev = header;
            } else { // saat ada lebih dari 1 node
                Tim.prev.next = Tim.next;
                Tim.next.prev = Tim.prev;
            }

            this.size -= 1;
            return Tim;
        }

        // Method digunakan untuk set Tim depan Sofita saat ini
        void setsofitaSekarang(Tim Tim) {
            sofitaSekarang = Tim;
        }

        Tim getSofitaSekarang(){
            return sofitaSekarang;
        }

        void setPenjokiNow(Tim Tim){
            jokiSaiki = Tim;
        }

        Tim getPenjokiNow(){
            return jokiSaiki;
        }

        void iniPenjokiAwal() {
            Tim nodeSekarang = header.next;
            Tim timPoinTerkecil = null;
            Tim timPoinKeduaKecil = null;
            Long poinTerkecil = Long.MAX_VALUE;
            Long poinKeduaTerkecil = Long.MAX_VALUE;
        
            for (int j = 0; j < LLTim.size; j++) {
                if (nodeSekarang.sumScore < poinTerkecil) {
                    timPoinKeduaKecil = timPoinTerkecil;
                    poinKeduaTerkecil = poinTerkecil;
                    timPoinTerkecil = nodeSekarang;
                    poinTerkecil = nodeSekarang.sumScore;
                } else if (nodeSekarang.sumScore < poinKeduaTerkecil) {
                    timPoinKeduaKecil = nodeSekarang;
                    poinKeduaTerkecil = nodeSekarang.sumScore;
                }
                nodeSekarang = nodeSekarang.next;
            }
        
            if (timPoinTerkecil == LLTim.sofitaSekarang) {
                LLTim.jokiSaiki = timPoinKeduaKecil;
            } else {
                LLTim.jokiSaiki = timPoinTerkecil;
            }
        }



        // Method digunakan untuk mengecek letak urutan Tim depan Sofita saat ini
        int getSofitaTimSortedNow() {
            Tim check = header.next;
            int counter = 0;
            while (!check.equals(sofitaSekarang)) {
                counter++;
                check = check.next;
            }
            // return counter;
            return counter + 1;
        }

        // Menggerakan Sofita ke kanan
        Tim gerakKananSofita() {
            if (this.size == 0) { // empty
                // do nothing
                throw new NullPointerException("LinkedList Size is 0");
            } else if (this.size == 1) { // cuma satu elemen
                // do nothing
            } else if (sofitaSekarang.next.equals(footer)) { // elemen terakhir
                sofitaSekarang = header.next;
            } else { // kasus normal
                sofitaSekarang = sofitaSekarang.next;
            }
            return sofitaSekarang;
        }

        // Menggerakan Sofita ke kiri
        Tim gerakKiriSofita() {
            if (this.size == 0) { // empty
                // do nothing
                throw new NullPointerException("LinkedList Size is 0");
            } else if (this.size == 1) { // cuma satu elemen
                // do nothing
            } else if (sofitaSekarang.prev.equals(header)) { // elemen pertama
                sofitaSekarang = footer.prev;
            } else { // kasus normal
                sofitaSekarang = sofitaSekarang.prev;
            }
            return sofitaSekarang;
        }


        // Menggerakan Sofita ke kanan
        Tim gerakKananPenjoki() {
            if (size == 0) { // empty
                // do nothing
                throw new NullPointerException("LinkedList Size is 0");
            } else if (size == 1) { // cuma satu elemen
                // do nothing
                return jokiSaiki;
            } else if (jokiSaiki.next.equals(footer)) { // elemen terakhir
                jokiSaiki = header.next;
            } else { // kasus normal
                jokiSaiki = jokiSaiki.next;
            }
            return jokiSaiki;
        }

        // Menggerakan Sofita ke kiri
        Tim gerakKiriPenjoki() {
            if (this.size == 0) { // empty
                // do nothing
                throw new NullPointerException("LinkedList Size is 0");
            } else if (this.size == 1) { // cuma satu elemen
                // do nothing
            } else if (jokiSaiki.prev.equals(header)) { // elemen pertama
                jokiSaiki = footer.prev;
            } else { // kasus normal
                jokiSaiki = jokiSaiki.prev;
            }
            return jokiSaiki;
        }




        // Pindah Tim sekaligus mereturn Tim untuk ditempati Sofita
        void pindahTim(Tim Tim) {
            if (this.size == 0) {
                // do nothing
            } else if (this.size == 1) { // cuma satu Tim permainan
                // do nothing
            } else if (Tim.next.equals(footer)) { // Tim berada paling kanan
                // Tim stay
                // Sofita pindah ke depan
                sofitaSekarang = header.next;
            } else { // sisanya
                // Sofita pindah ke kanannya
                sofitaSekarang = Tim.next;
                // pindah Tim ke pojok kanan
                Tim TimDipindah = remove(Tim);
                this.addLast(TimDipindah);
            }
        }

        // Clear all Tim
        void clear() {
            header.next = footer;
            footer.prev = header;
            this.size = 0;
        }

        // Menggabungkan dua subarray dari array Tim.
        // Subarray pertama adalah arr[l..m]
        // Subarray kedua adalah arr[m+1..r]
        // void merge(Tim[] arr, int l, int m, int r) {
        //     // Menentukan ukuran dari dua subarray yang akan digabungkan
        //     int n1 = m - l + 1;
        //     int n2 = r - m;

        //     /* Membuat array sementara */
        //     Tim[] L = new Tim[n1];
        //     Tim[] R = new Tim[n2];

        //     /* Menyalin data ke array sementara */
        //     for (int i = 0; i < n1; ++i)
        //         L[i] = arr[l + i];
        //     for (int j = 0; j < n2; ++j)
        //         R[j] = arr[m + 1 + j];

        //     /* Menggabungkan array sementara */

        //     // Index awal dari subarray pertama dan kedua
        //     int i = 0, j = 0;

        //     // Index awal dari array gabungan
        //     int k = l;
        //     while (i < n1 && j < n2) {
        //         // Membandingkan rerata dari Tim di subarray
        //         if (L[i].rerata > R[j].rerata) {
        //             arr[k] = L[i];
        //             i++;
        //         } else if (L[i].rerata == R[j].rerata) {
        //             // Jika rerata sama, bandingkan ID Tim
        //             if (L[i].id < R[j].id) {
        //                 arr[k] = L[i];
        //                 i++;
        //             } else {
        //                 arr[k] = R[j];
        //                 j++;
        //             }
        //         } else {
        //             arr[k] = R[j];
        //             j++;
        //         }
        //         k++;
        //     }

        //     /* Menyalin elemen yang tersisa dari L[] jika ada */
        //     while (i < n1) {
        //         arr[k] = L[i];
        //         i++;
        //         k++;
        //     }

        //     /* Menyalin elemen yang tersisa dari R[] jika ada */
        //     while (j < n2) {
        //         arr[k] = R[j];
        //         j++;
        //         k++;
        //     }
        // }

        // void mergesort(Tim[] arr, int l, int r) {
        //     // Mengurutkan array Tim menggunakan algoritma merge sort
        //     if (l < r) {
        //         // Menemukan titik tengah
        //         int m = l + (r - l) / 2;

        //         // Mengurutkan paruh pertama dan kedua
        //         mergesort(arr, l, m);
        //         mergesort(arr, m + 1, r);

        //         // Menggabungkan kedua paruh yang telah diurutkan
        //         merge(arr, l, m, r);
        //     }
        // }

        // // Metode untuk mengurutkan Tim
        // Tim[] sort() {
        //     // Jika LinkedList tidak kosong
        //     // Membuat array Tim sesuai dengan ukuran LinkedList
        //     Tim[] arr = new Tim[this.size];
        //     Tim current = header.next;
        //     // Memasukkan semua Tim ke dalam array
        //     for(int i = 0; i < this.size; i++) {
        //         current.updateRerata();
        //         arr[i] = current;
        //         current = current.next;
        //     }
        //     // Mengurutkan array dengan algoritma merge sort
        //     mergesort(arr, 0, this.size - 1);
        //     return arr;
        // }


}



static class Node { // AVL Node
    long key, height, count; // key => score, count => banyaknya node pada suatu subtree dengan root == node
    Node left, right;
    long jumlahSama; // jumlah isi key yg sama (duplicate)
    int jumlahCurang = 0;

    Peserta peserta;
    // long jumlahPertandingan; // Added jumlahPertandingan field

    int id;


    Node(Peserta peserta) {
        this.peserta = peserta;
        this.key = peserta.poinPeserta; // Set key based on points
        this.height = 1;
        this.count = 1;
        this.jumlahSama = 1;

        // this.id = id; // Set id
        // this.jumlahPertandingan = jumlahPertandingan; // Set jumlahPertandingan

    }
}

static class AVLTree {

    Node root;

    // Method in-order traversal untuk menambahkan skor peserta ke dalam array
    public static void untukCekPoinUnik(Node node, long[] skorPeserta, int[] index) {
        if (node != null) {
            untukCekPoinUnik(node.left, skorPeserta, index); // Traverse left subtree
            skorPeserta[index[0]++] = node.key; // Tambahkan poin peserta ke dalam array
            untukCekPoinUnik(node.right, skorPeserta, index); // Traverse right subtree
        }
    }



    /* METHOD TAMBAHAN BUAT DEBUG POSISI JOKI */
    public void removeTopThree() {
        List<Node> topThree = new ArrayList<>();
        findTopThree(root, topThree);
        for (Node node : topThree) {
            // Delete using node's key and id
            delete(root, node.key, node.peserta.idPeserta);  // Pass the root, key, and id of the node
        }
    }
    // Helper method to find the top three participants
    private void findTopThree(Node node, List<Node> topThree) {
        if (node == null) return;
    
        findTopThree(node.right, topThree);
        if (topThree.size() < 3) {
            topThree.add(node);
        }
        findTopThree(node.left, topThree);
    }


    public void setAllPointsToOne() {
        setAllPointsToOne(root);
    }
    
    // Helper recursive method to set points to one
    private void setAllPointsToOne(Node node) {
        if (node == null) return;
        node.peserta.poinPeserta = 1;
        setAllPointsToOne(node.left);
        setAllPointsToOne(node.right);
    }
    




    /* UNTUK DEBUG */
    public void printPeserta() {
        // Assuming 'root' is the root of the AVL tree
        printPesertaInOrder(this.root);
    }
    
    // Helper method to traverse the AVL tree in-order and print participants
    private void printPesertaInOrder(Node node) {
        if (node == null) {
            return;
        }

        // Traverse the left subtree
        printPesertaInOrder(node.left);

        // Print the current participant's details
        // Assuming that node.key holds the score, and you have a mapping from key to participants
        System.out.println("Peserta dengan Skor: " + node.key);

        // Traverse the right subtree
        printPesertaInOrder(node.right);
    }



    // Implement right rotate

    Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights & count
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1;
        y.count = y.jumlahSama + getCount(y.left) + getCount(y.right);

        x.height = max(getHeight(x.left), getHeight(x.right)) + 1;
        x.count = x.jumlahSama + getCount(x.left) + getCount(x.right);

        // Return new root
        return x;
    }

    // Implement left rotate
    Node leftRotate(Node y) {
        Node x = y.right;
        Node T2 = x.left;

        // Perform rotation
        x.left = y;
        y.right = T2;

        // Update heights & count
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1;
        y.count = y.jumlahSama + getCount(y.left) + getCount(y.right);

        x.height = max(getHeight(x.left), getHeight(x.right)) + 1;
        x.count = x.jumlahSama + getCount(x.left) + getCount(x.right);

        // Return new root
        return x;
    }

    // // Implement insert node to AVL Tree
    // Node insert(Node node, long key, long id) {
    //     if (node == null) {
    //         return (new Node(key, id));
    //     }

    //     if (key < node.key) {
    //         node.left = insert(node.left, key, id);
    //     } else if (key > node.key) {
    //         node.right = insert(node.right, key, id);
    //     } else {
    //         //TODO: KALO POIN SAMA
    //         node.jumlahSama += 1;
    //         node.count += 1;
    //         return node;

    //     }

    //     // Update height & count
    //     node.height = 1 + max(getHeight(node.left), getHeight(node.right));
    //     node.count = node.jumlahSama + getCount(node.left) + getCount(node.right);

    //     // Get balance factor
    //     long balance = getBalance(node);

    //     // If this node becomes unbalanced, then there are 4 cases

    //     // Left Left Case
    //     if (balance > 1 && key < node.left.key) {
    //         return rightRotate(node);
    //     }

    //     // Right Right Case
    //     if (balance < -1 && key > node.right.key) {
    //         return leftRotate(node);
    //     }

    //     // Left Right Case
    //     if (balance > 1 && key > node.left.key) {
    //         node.left = leftRotate(node.left);
    //         return rightRotate(node);
    //     }

    //     // Right Left Case
    //     if (balance < -1 && key < node.right.key) {
    //         node.right = rightRotate(node.right);
    //         return leftRotate(node);
    //     }

    //     return node;
    // }



    Node insert(Node node, Peserta peserta) {
        if (node == null) {
            return new Node(peserta);
        }
    
        // Determine placement based on hierarchy: points, jumlahMatch, and idPeserta
        if (peserta.poinPeserta < node.peserta.poinPeserta || 
            (peserta.poinPeserta == node.peserta.poinPeserta && peserta.jumlahMatch < node.peserta.jumlahMatch) || 
            (peserta.poinPeserta == node.peserta.poinPeserta && peserta.jumlahMatch == node.peserta.jumlahMatch && peserta.idPeserta < node.peserta.idPeserta)) {
            node.left = insert(node.left, peserta);
        } else if (peserta.poinPeserta > node.peserta.poinPeserta || 
                   (peserta.poinPeserta == node.peserta.poinPeserta && peserta.jumlahMatch > node.peserta.jumlahMatch) || 
                   (peserta.poinPeserta == node.peserta.poinPeserta && peserta.jumlahMatch == node.peserta.jumlahMatch && peserta.idPeserta > node.peserta.idPeserta)) {
            node.right = insert(node.right, peserta);
        } else {
            // Handle duplicate points scenario
            node.jumlahSama += 1;
            node.count += 1;
            return node;
        }
    
        // Update height & count
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        node.count = node.jumlahSama + getCount(node.left) + getCount(node.right);
    
        // Get balance factor
        long balance = getBalance(node);
    
        // Perform rotations if the node becomes unbalanced
    
        // Left Left Case
        if (balance > 1 && (peserta.poinPeserta < node.left.peserta.poinPeserta || 
                            (peserta.poinPeserta == node.left.peserta.poinPeserta && peserta.jumlahMatch < node.left.peserta.jumlahMatch) || 
                            (peserta.poinPeserta == node.left.peserta.poinPeserta && peserta.jumlahMatch == node.left.peserta.jumlahMatch && peserta.idPeserta < node.left.peserta.idPeserta))) {
            return rightRotate(node);
        }
    
        // Right Right Case
        if (balance < -1 && (peserta.poinPeserta > node.right.peserta.poinPeserta || 
                             (peserta.poinPeserta == node.right.peserta.poinPeserta && peserta.jumlahMatch > node.right.peserta.jumlahMatch) || 
                             (peserta.poinPeserta == node.right.peserta.poinPeserta && peserta.jumlahMatch == node.right.peserta.jumlahMatch && peserta.idPeserta > node.right.peserta.idPeserta))) {
            return leftRotate(node);
        }
    
        // Left Right Case
        if (balance > 1 && (peserta.poinPeserta > node.left.peserta.poinPeserta || 
                            (peserta.poinPeserta == node.left.peserta.poinPeserta && peserta.jumlahMatch > node.left.peserta.jumlahMatch) || 
                            (peserta.poinPeserta == node.left.peserta.poinPeserta && peserta.jumlahMatch == node.left.peserta.jumlahMatch && peserta.idPeserta > node.left.peserta.idPeserta))) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
    
        // Right Left Case
        if (balance < -1 && (peserta.poinPeserta < node.right.peserta.poinPeserta || 
                             (peserta.poinPeserta == node.right.peserta.poinPeserta && peserta.jumlahMatch < node.right.peserta.jumlahMatch) || 
                             (peserta.poinPeserta == node.right.peserta.poinPeserta && peserta.jumlahMatch == node.right.peserta.jumlahMatch && peserta.idPeserta < node.right.peserta.idPeserta))) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
    
        return node;
    }
    





    // // Implement insert node to AVL Tree
    // Node insert(Node node, long key, long id, long jumlahPertandingan) {
    //     if (node == null) {
    //         return (new Node(key, id, jumlahPertandingan));
    //     }

    //     // Insert based on the key (poinPeserta)
    //     if (key > node.key) {
    //         node.left = insert(node.left, key, id, jumlahPertandingan);
    //     } else if (key < node.key) {
    //         node.right = insert(node.right, key, id, jumlahPertandingan);
    //     } else {
    //         // If the keys (points) are the same, check by jumlahPertandingan
    //         if (jumlahPertandingan < node.jumlahPertandingan) {
    //             node.left = insert(node.left, key, id, jumlahPertandingan);
    //         } else if (jumlahPertandingan > node.jumlahPertandingan) {
    //             node.right = insert(node.right, key, id, jumlahPertandingan);
    //         } else {
    //             // If both points and jumlahPertandingan are the same, compare by ID
    //             if (id < node.id) {
    //                 node.left = insert(node.left, key, id, jumlahPertandingan);
    //             } else {
    //                 node.right = insert(node.right, key, id, jumlahPertandingan);
    //             }
    //         }
    //     }

    //     // Update height & count
    //     node.height = 1 + max(getHeight(node.left), getHeight(node.right));
    //     node.count = node.jumlahSama + getCount(node.left) + getCount(node.right);

    //     // Get balance factor
    //     long balance = getBalance(node);

    //     // If this node becomes unbalanced, then there are 4 cases

    //     // Left Left Case
    //     if (balance > 1 && key < node.left.key) {
    //         return rightRotate(node);
    //     }

    //     // Right Right Case
    //     if (balance < -1 && key > node.right.key) {
    //         return leftRotate(node);
    //     }

    //     // Left Right Case
    //     if (balance > 1 && key > node.left.key) {
    //         node.left = leftRotate(node.left);
    //         return rightRotate(node);
    //     }

    //     // Right Left Case
    //     if (balance < -1 && key < node.right.key) {
    //         node.right = rightRotate(node.right);
    //         return leftRotate(node);
    //     }

    //     return node;
    // }



    Node delete(Node root, long key, long id)
    {
        // STEP 1: PERFORM STANDARD BST DELETE
        if (root == null)
            return root;

        // If the key to be deleted is smaller than
        // the root's key, then it lies in left subtree
        if (key < root.key)
            root.left = delete(root.left, key, id);

            // If the key to be deleted is greater than the
            // root's key, then it lies in right subtree
        else if (key > root.key)
            root.right = delete(root.right, key, id);

            // if key is same as root's key, then this is the node
            // to be deleted
        else
        {
            // if jumlah sama masih ada rootnya jangan diilangin dulu gan, duplicatenya urusin
            if (root.jumlahSama >= 1) {
                root.jumlahSama -= 1;
                root.count -= 1;
                if (root.jumlahSama == 0){
                    if ((root.left == null) || (root.right == null)) {
                        root = (root.left == null) ? root.right : root.left;
                    }
                }
            } else {
                // node with only one child or no child
                if ((root.left == null) || (root.right == null)) {
                    root = (root.left == null) ? root.right : root.left;
                } else {
                    // node with two children: Get the inorder
                    // successor (smallest in the right subtree)
                    Node temp = lowerBound(root.right);

                    // Copy the inorder successor's data to this node
                    root.key = temp.key;
                    // fixing yg keupdate ga cuma key doang, ada count juga
                    root.jumlahSama = temp.jumlahSama;
                    root.count = temp.count;
                    // Delete the inorder successor
                    root.right = delete(root.right, temp.key, id);
                }
            }
        }

        // If the tree had only one node then return
        if (root == null)
            return root;

        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
        root.height = max(getHeight(root.left), getHeight(root.right)) + 1;
        root.count = root.jumlahSama + getCount(root.left) + getCount(root.right);

        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
        // this node became unbalanced)
        long balance = getBalance(root);

        // If this node becomes unbalanced, then there are 4 cases
        // Left Left Case
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        // Left Right Case
        if (balance > 1 && getBalance(root.left) < 0)
        {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // Right Right Case
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        // Right Left Case
        if (balance < -1 && getBalance(root.right) > 0)
        {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    // Mencari lowerBound dari suatu node
    Node lowerBound(Node node) {
        // Return node with the lowest from this node
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // Mencari upperBound dari suatu node
    Node upperBound(Node node) {
        // Return node with the greatest from this node
        Node current = node;
        while (current.right != null) {
            current = current.right;
        }
        return current;
    }


    // Utility function to get height of node
    long getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Utility function to get num of peoples
    long getCount(Node node) {
        if (node == null) {
            return 0;
        }
        return node.count;
    }

    // Utility function to get balance factor of node
    long getBalance(Node node) {
        if (node == null) {
            return 0;
        }

        return getHeight(node.left) - getHeight(node.right);
    }

    // Utility function to get max of 2 longs
    long max(long a, long b) {
        return (a > b) ? a : b;
    }

    // QUERY MAIN, LIHAT
    // Method digunakan untuk mencari jumlah score yang kurang dari inserted key
    long countBefore(Node node, long insertedKey) {
        if (node == null) { // Jika node kosong, return 0
            return 0;
        }
        // Jika sudah didapat insertedKey sama dengan key node, maka cari count di subtree kiri dulu
        if (node.key == insertedKey) {
            return node.jumlahSama + getCount(node.left) - 1;
        }
        // Jika insertedKey lebih kecil dari key node, maka cari di subtree kiri
        if (node.key < insertedKey) {
            // Cek kiri lalu, ke kanan
            if (node.left != null) {
                // Jika ada node di subtree kiri, maka cari count di subtree kiri + duplicatenya
                return node.jumlahSama + node.left.count + countBefore(node.right, insertedKey);
            } else {
                return node.jumlahSama + countBefore(node.right, insertedKey);
            }
        }
        // Ke kiri untuk cari key yang cocok
        return countBefore(node.left, insertedKey);
    }

    // Method digunakan untuk mencari score max
    Node findMax() {
        Node temp = root;
        while (temp.right != null) {
            temp = temp.right;
        }
        return temp;
    }

    Node findMin() {
        Node temp = root;
        while (temp.left != null) {
            temp = temp.left;
        }
        return temp;
    }
}
}