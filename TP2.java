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
    static int totalPeserta = 0; // Variabel global untuk melacak total peserta


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
                if (timASekarang == null) {
                    out.println(-1);
                } else {
                    for (int j = 0; j < jumlahPesertaBaru; j++) {
                        int id = idPeserta++;
                        int poinBaru = 3; // Assuming 3 as the new participant's poin

                        // Create a new Peserta object and insert it
                        Peserta pesertaBaru = new Peserta(id, poinBaru);
                        timASekarang.treePeserta.root = timASekarang.treePeserta.insert(timASekarang.treePeserta.root, pesertaBaru);
                    }
                    timASekarang.jumlahPeserta += jumlahPesertaBaru;
                    out.println(timASekarang.jumlahPeserta);
                }
            }
            else if (query.equals("E")) {
                long minPoin = in.nextLong();  // Batas minimum poin
                boolean sofitaEliminasi = false;  // Apakah tim Sofita dieliminasi
                boolean jokiEliminasi = false;  // Apakah tim Joki dieliminasi
                int countElim = 0;  // Jumlah tim yang dieliminasi
            
                Tim currentE = LLTim.header.next;
                Tim sofitaTeam = LLTim.getSofitaSekarang();
                Tim jokiTeam = LLTim.getPenjokiNow();
            
                // Iterasi untuk mengeliminasi tim
                while (currentE != LLTim.footer) {
                    Tim nextTim = currentE.next;  // Simpan referensi tim berikutnya
            
                    if (currentE.sumScore < minPoin) {
                        countElim++;  // Tambah jumlah tim yang dieliminasi
            
                        // Jika tim yang dieliminasi adalah tim Sofita
                        if (currentE == sofitaTeam) {
                            sofitaEliminasi = true;
                        }
            
                        // Jika tim yang dieliminasi adalah tim Joki
                        if (currentE == jokiTeam) {
                            jokiEliminasi = true;
                        }
            
                        // Hapus tim dari daftar
                        LLTim.remove(currentE);
                    }
                    currentE = nextTim;
                }
            
                // Jika Sofita dieliminasi, pindah ke tim skor tertinggi
                if (sofitaEliminasi && LLTim.size > 0) {
                    Tim skorTertinggiTim = LLTim.cariSkorTimHighest();
                    LLTim.setsofitaSekarang(skorTertinggiTim);
                }
            
                // Jika Joki dieliminasi, pindah ke tim skor terendah
                if (jokiEliminasi && LLTim.size > 0) {
                    Tim skorTerendahTim = LLTim.cariSkorTimLowest();
                    LLTim.setPenjokiNow(skorTerendahTim);
                }
            
                // Pastikan Joki tidak pindah lagi jika dia sudah berpindah karena konflik dengan Sofita
                if (sofitaEliminasi && jokiEliminasi && LLTim.size > 0) {
                    Tim skorTerendahTim = LLTim.cariSkorTimLowest();
                    if (LLTim.getPenjokiNow() != skorTerendahTim) {
                        LLTim.setPenjokiNow(skorTerendahTim);
                    }
                }
            
                out.println(countElim);  // Cetak jumlah tim yang dieliminasi
            }
            

            else if (query.equals("G")) {
                String posisi = in.next();  // Posisi "L" atau "R" (Kiri atau Kanan)
                Tim timSekarang = LLTim.getSofitaSekarang();  // Ambil tim yang diawasi Sofita
                
                if (timSekarang == null) {
                    // Jika Sofita belum ada, buat tim baru
                    AVLTree treePesertaBaru = new AVLTree();
                    int idTimBaru = idTim++; // ID tim baru
                    Tim timBaru = new Tim(treePesertaBaru, 7, idTimBaru);  // Tim baru dengan 7 peserta
                    
                    // Tambahkan 7 peserta baru dengan poin 1
                    for (int a = 0; a < 7; i++) {
                        Peserta pesertaBaru = new Peserta(idPeserta++, 1);  // Poin awal 1
                        timBaru.treePeserta.root = timBaru.treePeserta.insert(timBaru.treePeserta.root, pesertaBaru);
                    }
                    
                    // Sisipkan tim baru ke linked list (kiri atau kanan)
                    if (posisi.equals("L")) {
                        // Sisipkan sebelum tim pertama (karena Sofita null)
                        Tim firstTim = LLTim.getFirst();
                        timBaru.next = firstTim;
                        timBaru.prev = firstTim.prev;
                        firstTim.prev.next = timBaru;
                        firstTim.prev = timBaru;
                    } else {
                        // Sisipkan setelah tim pertama (karena Sofita null)
                        Tim firstTim = LLTim.getFirst();
                        timBaru.prev = firstTim;
                        timBaru.next = firstTim.next;
                        firstTim.next.prev = timBaru;
                        firstTim.next = timBaru;
                    }
                    
                    LLTim.size++;  // Increment jumlah tim dalam linked list
                    
                    // Sofita langsung masuk ke tim pertama
                    LLTim.setsofitaSekarang(timBaru);
                    
                    out.println(idTimBaru);  // Cetak ID tim baru
                } else {
                    // Jika Sofita sudah ada, buat tim baru di posisi yang diminta
                    AVLTree treePesertaBaru = new AVLTree();
                    int idTimBaru = idTim++; // ID tim baru
                    Tim timBaru = new Tim(treePesertaBaru, 7, idTimBaru);  // Tim baru dengan 7 peserta
                    
                    // Tambahkan 7 peserta baru dengan poin 1
                    for (int a = 0; a < 7; a++) {
                        Peserta pesertaBaru = new Peserta(idPeserta++, 1);  // Poin awal 1
                        timBaru.treePeserta.root = timBaru.treePeserta.insert(timBaru.treePeserta.root, pesertaBaru);
                    }
                    
                    // Sisipkan tim baru ke posisi yang diminta (kiri atau kanan)
                    if (posisi.equals("L")) {
                        // Sisipkan sebelum tim yang diawasi Sofita
                        Tim prev = timSekarang.prev;
                        timBaru.next = timSekarang;
                        timBaru.prev = prev;
                        prev.next = timBaru;
                        timSekarang.prev = timBaru;
                    } else {
                        // Sisipkan setelah tim yang diawasi Sofita
                        Tim next = timSekarang.next;
                        timBaru.prev = timSekarang;
                        timBaru.next = next;
                        next.prev = timBaru;
                        timSekarang.next = timBaru;
                    }
                    
                    LLTim.size++;  // Increment jumlah tim dalam linked list
                    
                    // Update Joki jika perlu
                    if (timBaru.sumScore < LLTim.getPenjokiNow().sumScore) {
                        LLTim.setPenjokiNow(timBaru);  // Tim baru menjadi Joki
                    }
                    
                    out.println(idTimBaru);  // Cetak ID tim baru
                }
            }
            
        

            else if (query.equals("M")) {
                String arah = in.next();  // Get movement direction: 'L' or 'R'
                Tim timSekarang = LLTim.getSofitaSekarang();  // Get Sofita's current team
                
                if (timSekarang == null) {
                    out.println(-1);
                    continue;
                }
            
                // Move Sofita based on direction
                if (arah.equals("R")) {
                    LLTim.gerakKananSofita();
                } else if (arah.equals("L")) {
                    LLTim.gerakKiriSofita();
                }
            
                // Check if Sofita meets Joki
                if (LLTim.getSofitaSekarang() == LLTim.getPenjokiNow()) {
                    Tim currentTeam = LLTim.getSofitaSekarang();
                    currentTeam.sofiKetemuJoki++;
            
                    // Handle based on number of encounters
                    if (currentTeam.sofiKetemuJoki == 1) {
                        // Remove top 3 scoring participants
                        for (int a = 0; a < 3; a++) {
                            Node highestNode = currentTeam.treePeserta.findMax();
                            if (highestNode != null) {
                                Peserta highestScorer = highestNode.peserta;
                                currentTeam.treePeserta.root = currentTeam.treePeserta.delete(
                                    currentTeam.treePeserta.root, 
                                    highestScorer.poinPeserta,
                                    highestScorer.idPeserta
                                );
                                currentTeam.jumlahPeserta--;
                                currentTeam.sumScore -= highestScorer.poinPeserta;
                            }
                        }
            
                        // Check if team should be eliminated
                        if (currentTeam.jumlahPeserta < 7) {
                            LLTim.remove(currentTeam);
                            if (LLTim.size == 0) {
                                out.println(-1);
                                continue;
                            }
                        }
                    } 
                    else if (currentTeam.sofiKetemuJoki == 2) {
                        // Create new AVL tree with all scores set to 1
                        AVLTree newTree = new AVLTree();
                        Node currentNode = currentTeam.treePeserta.root;
                        int totalPeserta = currentTeam.jumlahPeserta;
                        
                        // Reset scores using a new tree
                        while (current != null) {
                            Peserta p = currentNode.peserta;
                            p.poinPeserta = 1;
                            newTree.root = newTree.insert(newTree.root, p);
                            currentNode = currentNode.right; // Move to next node
                        }
                        currentTeam.treePeserta = newTree;
                        currentTeam.sumScore = totalPeserta; // All scores are 1
                    } 
                    else if (currentTeam.sofiKetemuJoki == 3) {
                        // Eliminate the team
                        LLTim.remove(currentTeam);
                        if (LLTim.size == 0) {
                            out.println(-1);
                            continue;
                        }
                    }
            
                    // Move Sofita to highest scoring team
                    Tim highestTeam = null;
                    long highestScore = Long.MIN_VALUE;
                    Tim currentNode = LLTim.getFirst();
                    
                    while (currentNode != LLTim.footer) {
                        if (currentNode.sumScore > highestScore) {
                            highestScore = currentNode.sumScore;
                            highestTeam = currentNode;
                        }
                        currentNode = currentNode.next;
                    }
                    
                    if (highestTeam != null) {
                        LLTim.setsofitaSekarang(highestTeam);
                    }
            
                    // Find new team with lowest score for Joki
                    Tim lowestTeam = null;
                    long lowestScore = Long.MAX_VALUE;
                    currentNode = LLTim.getFirst();
                    
                    while (currentNode != LLTim.footer) {
                        if (currentNode != LLTim.getSofitaSekarang() && currentNode.sumScore < lowestScore) {
                            lowestScore = currentNode.sumScore;
                            lowestTeam = currentNode;
                        }
                        currentNode = currentNode.next;
                    }
                    
                    if (lowestTeam != null) {
                        LLTim.setPenjokiNow(lowestTeam);
                    }
                }
            
                LLTim.debugInfo(LLTim.getSofitaSekarang(), LLTim.getPenjokiNow());
                // Output current Sofita's team ID
                out.println(LLTim.getSofitaSekarang().idTim);
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
                        // Panggil method iniPenjokiAwal untuk memindahkan penjoki ke tim dengan skor terendah atau kedua terendah
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
                    
                    if (root != null) {
                        long[] skorPeserta = new long[1000]; // Array dengan ukuran tetap untuk menyimpan skor
                        int[] index = {0}; // Gunakan array 1-elemen untuk menjaga index sebagai counter
                        AVLTree.untukCekPoinUnik(root, skorPeserta, index);
        
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
        }

        public List<Peserta> getTopPeserta(int topCount) {
            List<Peserta> allPeserta = this.treePeserta.getAllPeserta();
            allPeserta.sort((p1, p2) -> {
                if (p1.poinPeserta != p2.poinPeserta) return Integer.compare(p2.poinPeserta, p1.poinPeserta);
                if (p1.jumlahMatch != p2.jumlahMatch) return Integer.compare(p1.jumlahMatch, p2.jumlahMatch);
                return Integer.compare(p1.idPeserta, p2.idPeserta);
            });
            return allPeserta.subList(0, Math.min(topCount, allPeserta.size()));
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

        public int getId(){
            return idPeserta;
        }

        public int getPoin(){
            return poinPeserta;
        }

        public int getJumlahMatch(){
            return jumlahMatch;
        }
    }

    // Class DaftarTim digunakan untuk menyimpan semua Tim
    static class CircularDoublyLL<E> {
        int size; // Jumlah Tim
        Tim header, footer;
        Tim sofitaSekarang; // untuk menyimpan lokasi Sofita
        Tim jokiSaiki; // posisi joki
        Map<Integer, Tim> hashMap; // Menyimpan informasi tentang Tim berdasarkan ID

        // construct empty list
        CircularDoublyLL() {
            this.size = 0;
            this.header = new Tim(null, 0, 0);
            this.footer = new Tim(null, 0, 0);
            this.hashMap = new HashMap<>();
        }

        // Tambahkan metode getHashMapSize
        public int getHashMapSize() {
            return this.hashMap.size();
        }

        
        public int getTeamCount() {
            if (header.next == null || footer.prev == null) {
                return 0;  // Atau penanganan lain jika tim kosong
            }
            int count = 0;
            Tim current = header.next;
            while (current != footer) {
                count++;
                current = current.next;
            }
            return count;
        }
        
        public List<Integer> getSortedTeams() {
            List<Integer> sortedTeams = new ArrayList<>();
            Tim current = header;
            do {
                sortedTeams.add(current.idTim);
                current = current.next;
            } while (current != header);
            Collections.sort(sortedTeams); // Sesuaikan urutan sesuai kebutuhan
            return sortedTeams;
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

            } else { 
                footer.prev.next = Tim;
                Tim.prev = footer.prev;
                Tim.next = footer;
                footer.prev = Tim;
            }

            this.size += 1;
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


        Tim cariSkorTimLowest() {
            if (header.next == footer) {
                return null; // Tidak ada tim dalam daftar
            }
        
            Tim current = header.next;
            Tim lowestScoreTeam = current; // Awalnya anggap tim pertama memiliki skor terendah
        
            // Iterasi melalui seluruh tim
            while (current != footer) {
                if (current.sumScore < lowestScoreTeam.sumScore) {
                    lowestScoreTeam = current; // Perbarui jika ditemukan skor lebih rendah
                } else if (current.sumScore == lowestScoreTeam.sumScore) {
                    // Jika skor sama, prioritaskan berdasarkan ID terkecil
                    if (current.idTim < lowestScoreTeam.idTim) {
                        lowestScoreTeam = current;
                    }
                }
                current = current.next;
            }
        
            return lowestScoreTeam; // Tim dengan skor terendah
        }

        Tim findLowestScoringTeam(CircularDoublyLL<Tim> LLTim, Tim excludeTeam) {
            Tim lowestTeam = null;
            long lowestScore = Long.MAX_VALUE;
            Tim current = LLTim.getFirst();
            
            while (current != LLTim.footer) {
                if (current != excludeTeam && current.sumScore < lowestScore) {
                    lowestScore = current.sumScore;
                    lowestTeam = current;
                }
                current = current.next;
            }
            
            return lowestTeam;
        }

        

        void debugInfo(Tim sofitasPointer, Tim penjokiPointer) {
            StringBuilder sb = new StringBuilder();
        
            // Header debug
            sb.append("\n═════════════════════════════════════════════════════\n")
              .append("                   DEBUG INFORMATION                 \n")
              .append("═════════════════════════════════════════════════════\n");
        
            // Informasi umum
            sb.append("Team Size          : ").append(getTeamCount()).append("\n")
              .append("HashMap Size       : ").append(getHashMapSize()).append("\n")
              .append("Head               : ").append(header.next.idTim).append("\n")
              .append("Tail               : ").append(footer.prev.idTim).append("\n");
        
            // Tim yang sudah diurutkan
            sb.append("═════════════════════════════════════════════════════\n")
              .append("Sorted Teams       : ").append(getSortedTeams()).append("\n");
        
            // Pointer
            sb.append("═════════════════════════════════════════════════════\n")
              .append("Sofita Pointer     : ").append(sofitasPointer != null ? sofitasPointer.idTim : "null").append("\n")
              .append("Penjoki Pointer    : ").append(penjokiPointer != null ? penjokiPointer.idTim : "null").append("\n");
        
            // Detail setiap tim
            Tim current = header.next;
            while (current != footer) {
                sb.append("═════════════════════════════════════════════════════\n")
                  .append("Team ").append(current.idTim)
                  .append("          | Total Points: ").append(current.sumScore)
                  .append("    | Members: ").append(current.jumlahPeserta)
                  .append("   | SortedPeserta: ").append(current.treePeserta.size()).append("\n");
        
                // Top 3 peserta
                sb.append("   └─ Top 3 Participants:\n");
                List<Peserta> topPeserta = current.getTopPeserta(3);
                for (Peserta peserta : topPeserta) {
                    sb.append("      ├── ID: ").append(peserta.idPeserta)
                      .append("    | Points: ").append(peserta.poinPeserta)
                      .append("     | Matches: ").append(peserta.jumlahMatch).append("\n");
                }
        
                // Semua peserta aktif
                sb.append("   └─ All Active Participants:\n");
                for (Peserta peserta : current.treePeserta.getAllPeserta()) {
                    sb.append("      ├── ID: ").append(peserta.idPeserta)
                      .append("    | Points: ").append(peserta.poinPeserta)
                      .append("     | Matches: ").append(peserta.jumlahMatch).append("\n");
                }
        
                // Struktur AVL Tree
                sb.append("═════════════════════════════════════════════════════\n")
                  .append("                   AVL TREE STRUCTURE                \n")
                  .append("═════════════════════════════════════════════════════\n");
                sb.append(current.treePeserta.getAVLTreeStructure());
        
                current = current.next;
            }
        
            // Output debug
            System.out.println(sb.toString());
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

        Tim cariSkorTimHighest(){
            Tim current = LLTim.header.next;
            Tim timHighest = current;
            long highestSkor = current.sumScore;
            while(current != LLTim.footer){
                if (current.sumScore > highestSkor){
                    highestSkor = current.sumScore;
                    timHighest = current;
                }
            }
            return timHighest;
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
    }
}

static class AVLTree {

    Node root;

    // Method untuk menambahkan skor peserta ke dalam array
    public static void untukCekPoinUnik(Node node, long[] skorPeserta, int[] index) {
        if (node != null) {
            untukCekPoinUnik(node.left, skorPeserta, index); // Traverse left subtree
            skorPeserta[index[0]++] = node.key; // Tambahkan poin peserta ke dalam array
            untukCekPoinUnik(node.right, skorPeserta, index); // Traverse right subtree
        }
    }

    public int size() {
        return size(root); // Memanggil rekursi untuk menghitung ukuran pohon
    }
    
    private int size(Node root) {
        if (root == null) return 0;
        return 1 + size(root.left) + size(root.right);
    }
    
    public List<Peserta> getAllPeserta() {
        List<Peserta> pesertaList = new ArrayList<>();
        inorderTraversal(root, pesertaList);
        return pesertaList;
    }
    
    private void inorderTraversal(Node node, List<Peserta> pesertaList) {
        if (node != null) {
            inorderTraversal(node.left, pesertaList);
            pesertaList.add(node.peserta);
            inorderTraversal(node.right, pesertaList);
        }
    }
    
    public String getAVLTreeStructure() {
        StringBuilder sb = new StringBuilder();
        buildTreeString(root, "", true, sb);
        return sb.toString();
    }
    
    private void buildTreeString(Node node, String prefix, boolean isTail, StringBuilder sb) {
        if (node != null) {
            sb.append(prefix).append(isTail ? "└── " : "├── ").append(node.peserta.idPeserta).append("|")
              .append(node.peserta.jumlahMatch).append("|").append(node.peserta.poinPeserta).append("\n");
            buildTreeString(node.left, prefix + (isTail ? "    " : "│   "), false, sb);
            buildTreeString(node.right, prefix + (isTail ? "    " : "│   "), true, sb);
        }
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