import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

class Node {
    int id;
    int poin;
    Node next;

    public Node(int id, int poin) {
        this.id = id;
        this.poin = poin;
        this.next = null;
    }
}

class LinkedList {
    private Node head;
    private Node tail;
    private int ukuran;

    public LinkedList() {
        head = null;
        tail = null;
        ukuran = 0;
    }

    public void tambah(int id, int poin) {
        Node pesertaBaru = new Node(id, poin);
        if (head == null) {
            head = pesertaBaru;
            tail = pesertaBaru;
        } else {
            tail.next = pesertaBaru;
            tail = pesertaBaru;
        }
        ukuran++;
    }


    // T
    public Node cari(int idPeserta) {
        Node current = head;
        while (current != null) {
            if (current.id == idPeserta) {
                return current;
            }
            current = current.next;
        }
        return null; // Tidak ditemukan
    }


    // B
    public int[] getPoinArray() {
        int[] poinArray = new int[ukuran];
        Node current = head;
        int index = 0;
        while (current != null) {
            poinArray[index++] = current.poin;
            current = current.next;
        }
        return poinArray;
    }




    public int getUkuran() {
        return ukuran;
    }
}

class Tim {
    LinkedList peserta;
    int idTim;

    public Tim(int idTim) {
        this.idTim = idTim;
        this.peserta = new LinkedList();
    }

    public void tambahPeserta(int jumlahPeserta, int poinAwal, int idAwal) {
        for (int i = 0; i < jumlahPeserta; i++) {
            peserta.tambah(idAwal + i, poinAwal);
        }
    }

    public int getJumlahPeserta() {
        return peserta.getUkuran();
    }


    // B
    public int hitungOutlier(int extremeBound) {
        int[] poinArray = peserta.getPoinArray();
        insertionSort(poinArray);

        int K = poinArray.length;
        int indexQ1 = Math.max(0, (K - 1) / 4);
        int indexQ3 = Math.min(K - 1, 3 * (K - 1) / 4);

        int Q1 = poinArray[indexQ1];
        int Q3 = poinArray[indexQ3];
        int IQR = Q3 - Q1;
        int L = Q1 - (int) Math.floor(1.5 * IQR);
        int U = Q3 + (int) Math.floor(1.5 * IQR);

        int count = 0;
        if (extremeBound > 0) {  // Jika EXTREME_BOUND = U, hitung poin > U
            for (int poin : poinArray) {
                if (poin > U) count++;
            }
        } else {  // Jika EXTREME_BOUND = L, hitung poin < L
            for (int poin : poinArray) {
                if (poin < L) count++;
            }
        }
        return count;
    }

    // B
    private void insertionSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }


}

public class bismillah {
    private static InputReader in;
    private static PrintWriter out;
    private static int M;  // Jumlah tim
    private static Tim[] tims;
    private static int posisiSofita;  // Menyimpan posisi tim yang sedang diawasi Sofita (index-based)


    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        M = in.nextInteger();  // 1. Input Banyaknya tim
        tims = new Tim[M + 1];  // Array tim, indeks mulai dari 1

        int idPeserta = 1;
        for (int i = 0; i <= M; i++) {
            tims[i] = new Tim(i);
            int jumlahPeserta = in.nextInteger(); //2. Input jumlah peserta dari tim ke-i
            tims[i].tambahPeserta(jumlahPeserta, 0, idPeserta);
            idPeserta += jumlahPeserta;
        }

        // Membaca poin peserta
        for (int i = 0; i <= M; i++) {
            Tim tim = tims[i]; // Ambil tim ke-i
            for (int j = 0; j <= tim.getJumlahPeserta(); j++) { // Ambil poin untuk setiap peserta di tim tersebut
                int poinPeserta = in.nextInteger(); // Poin awal dari peserta
                tim.peserta.tambah(j, poinPeserta); // ID peserta berurutan dalam satu tim
            }
        }


        posisiSofita = 1; //Mulai dari tim pertama
        int O = in.nextInteger();  // 4. Read input banyak perintah
        for (int i = 0; i < O; i++) {
            String kegiatan = in.next(); //5. Read input aktivitasnya
            switch (kegiatan) {
                case "A":
                int jumlahPeserta = in.nextInteger();
                if (posisiSofita < 1 || posisiSofita > M) {  // periksa apakah Sofita mengawasi tim
                    out.println("-1");
                } else {
                    Tim timDiawasi = tims[posisiSofita];
                    timDiawasi.tambahPeserta(jumlahPeserta, 3, idPeserta);
                    idPeserta += jumlahPeserta;
                    out.println(timDiawasi.getJumlahPeserta());
                }
                break;

                case "B":
                    String boundType = in.next();
                    int idTimDiawasiB = in.nextInteger();
                    Tim timDiawasi = tims[idTimDiawasiB];
                    int extremeBound = boundType.equals("U") ? 1 : -1;
                    int jumlahOutlier = timDiawasi.hitungOutlier(extremeBound);
                    out.println(jumlahOutlier);
                    break;

                case "M":
                    String arah = in.next();
                    if(arah.equals("L")){
                        moveLeft();
                    } else if(arah.equals("R")){
                        moveRight();
                    }
                    break;

                case "T":
                    int idPengirim = in.nextInteger();
                    int idPenerima = in.nextInteger();
                    int jumlahPoin = in.nextInteger();
                    transferPoin(idPengirim, idPenerima, jumlahPoin);
                    break;
                    
            
                default:
                    break;
            }
            
        }

        out.flush();
        out.close();
    }


    // Ini harusnya ga di sini gasi bukannya taro di node harusnya
    private static void moveLeft() {
        if (posisiSofita == 1) {
            posisiSofita = M;  // Jika di tim pertama, pindah ke tim terakhir
        } else {
            posisiSofita--;  // Pindah ke tim sebelah kiri
        }
    }

    private static void moveRight() {
        if (posisiSofita == M) {
            posisiSofita = 1;  // Jika di tim terakhir, pindah ke tim pertama
        } else {
            posisiSofita++;  // Pindah ke tim sebelah kanan
        }
    }



    // T
    private static void transferPoin(int idPengirim, int idPenerima, int jumlahPoin) {
        Tim timSofita = tims[posisiSofita];
        Node pengirim = timSofita.peserta.cari(idPengirim);
        Node penerima = timSofita.peserta.cari(idPenerima);

        if (pengirim == null || penerima == null) {
            out.println("-1");  // ID peserta tidak ditemukan
            return;
        }

        if (jumlahPoin >= pengirim.poin) {
            out.println("-1");  // Jumlah poin yang dikirim lebih besar atau sama dengan poin pengirim
            return;
        }

        // Lakukan transfer poin
        pengirim.poin -= jumlahPoin;
        penerima.poin += jumlahPoin;

        out.println(pengirim.poin + " " + penerima.poin);  // Cetak poin pengirim dan penerima setelah transfer
    }



    static class InputReader {
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

        public int nextInteger() {
            return Integer.parseInt(next());
        }
    }
}
