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
    static CircDoublyLL<Tim> LLTim = new CircDoublyLL<>();
    static int totalPeserta = 0; 

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
            for(long j = 0; j < current.jumlahPeserta ; j++){
                int poin = in.nextInt();
                int id = idPeserta;
                id++;
                sumScore += poin;
                current.treePeserta.root = current.treePeserta.insert(current.treePeserta.root, poin, id);
            }
            current.sumScore = sumScore;
        }

        LLTim.setsofitaSekarang(LLTim.header.next);
        LLTim.iniPenjokiAwal();

        int o = in.nextInt();
        for (int i = 0; i < o ; i++){
            String query = in.next();


        if (query.equals("A")) {
            if (LLTim.sofitaSekarang == null) {
                out.println(-1);
            } else {
                int jumlahPesertaBaru = in.nextInt();
                Tim currentTim = LLTim.sofitaSekarang;
                
                // Add new participants with score 3
                for (int j = 0; j < jumlahPesertaBaru; j++) {
                    currentTim.treePeserta.root = currentTim.treePeserta.insert(
                        currentTim.treePeserta.root, 
                        3, // initial score for new participant
                        idPeserta
                    );
                    currentTim.sumScore += 3; // Update total score
                    idPeserta++;
                }

                currentTim.jumlahPeserta += jumlahPesertaBaru;
                
                out.println(currentTim.jumlahPeserta);
            }
        }

            else if (query.equals("E")) {
                long minPoin = in.nextLong();
                if(LLTim.size == 0){
                    return;
                }
                int countElim = 0;
                Tim currentE = LLTim.header.next;
                Tim timSofi = LLTim.getSofitaSekarang();
                boolean timKeelim = false;

                while(currentE != LLTim.footer){
                    Tim next = currentE.next;
                    if(currentE.sumScore < minPoin){
                        if(currentE == timSofi){
                            timKeelim = true;
                        }

                        LLTim.remove(currentE);
                        countElim++;
                    }
                    currentE = next;
                }
                if(timKeelim && LLTim.size > 0){
                    Tim timSkorTertinggi = LLTim.findTimHighestScoreM();
                    LLTim.setsofitaSekarang(timSkorTertinggi);
                }

                out.println(countElim);

            }


            else if (query.equals("G")) {
                String posisi = in.next(); // 'L' atau 'R'
            
                // Buat AVL Tree baru untuk tim baru
                AVLTree treePesertaBaru = new AVLTree();
                int jumlahPesertaBaru = 7; // Tim baru memiliki 7 peserta
                int totalPoinTim = 0;
            
                // Tambahkan 7 peserta dengan poin awal 1 ke AVL Tree
                for (int j = 0; j < jumlahPesertaBaru; j++) {
                    int id = idPeserta++;
                    treePesertaBaru.root = treePesertaBaru.insert(treePesertaBaru.root, 1, id);
                    totalPoinTim += 1; // Tambahkan poin ke total tim
                }
            
                // Buat tim baru 
                Tim timBaru = new Tim(treePesertaBaru, totalPoinTim, idTim++);
                timBaru.jumlahPeserta = jumlahPesertaBaru; // UPDATE JUMLAH PESERTAAA
                timBaru.sumScore = totalPoinTim;
            
                if (LLTim.size == 0) {
                    // Jika tidak ada tim di list, tambahkan sebagai tim pertama
                    LLTim.addFirst(timBaru);
                    LLTim.setsofitaSekarang(timBaru);
                } else {
                    // Tambahkan tim baru di kiri atau kanan Sofita
                    Tim sofitaTim = LLTim.getSofitaSekarang();
                    if (posisi.equals("L")) {
                        LLTim.addBefore(sofitaTim, timBaru);
                    } else {
                        LLTim.addAfter(sofitaTim, timBaru);
                    }
                }
            
                // DEBUG ID TIM, JUMLAH PESERTA DAN TOTAL POINNNNN
                // out.println("ini id tim " + timBaru.idTim + 
                //             " INI JUMLAH PESERTA: " + timBaru.jumlahPeserta + 
                //             " total poin tim " + timBaru.sumScore);
                out.println(timBaru.idTim);
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
            

            else if (query.equals("M")) {
                String arah = in.next();  // Get movement direction: 'L' or 'R'
                Tim timSekarang = LLTim.getSofitaSekarang();  // Get Sofita's current team
                
                if (timSekarang == null) {
                    out.println(-1);
                } else{
                    if(arah.equals("L")){
                        LLTim.gerakKiriSofita();
                    } else if(arah.equals("R")){
                        LLTim.gerakKananSofita();
                    }

                // Cek apakah Sofita sekarang mengawasi tim dengan penjoki
                Tim timBaruSofita = LLTim.getSofitaSekarang();
                if (timBaruSofita != null && timBaruSofita == LLTim.getPenjokiNow()) {
                    // Jika tim sekarang memiliki penjoki, lakukan tindakan yang sesuai (eliminasi tim)
                    LLTim.ketemu();  // Hapus atau modifikasi tim sesuai aturan kompetisi
                }


                    if(LLTim.size == 0 || LLTim.getSofitaSekarang() == null){
                        out.println(-1);
                    } else{
                        out.println(LLTim.sofitaSekarang.idTim);
                    }
                }

            }
        


            else if (query.equals("T")) {
                long idPengirim = in.nextLong(); 
                long idPenerima = in.nextLong();  
                long jumlahPoinKirim = in.nextLong(); 
                
                Tim timSekarang = LLTim.getSofitaSekarang();
                
                // Cek sofita
                if (timSekarang == null) {
                    out.println(-1);
                    continue;
                }
                
                
                // Nyari sender dan receiver
                Node pengirimNode = timSekarang.treePeserta.findNodeById(timSekarang.treePeserta.root, idPengirim);
                Node penerimaNode = timSekarang.treePeserta.findNodeById(timSekarang.treePeserta.root, idPenerima);
                
                // Cek apakah ada kedua peserta
                if (pengirimNode == null || penerimaNode == null) {
                    out.println(-1);
                    continue;
                }
                
                // Cek sender punya cukup poin
                if (jumlahPoinKirim >= pengirimNode.key) {
                    out.println(-1);
                    continue;
                }
                
                // nyimpen poin awal
                long poinPengirimAwal = pengirimNode.key;
                long poinPenerimaAwal = penerimaNode.key;
                
                // hitung poin baru
                long poinPengirimBaru = poinPengirimAwal - jumlahPoinKirim;
                long poinPenerimaBaru = poinPenerimaAwal + jumlahPoinKirim;
                
                // Delete kedua nodes dari AVL
                timSekarang.treePeserta.root = timSekarang.treePeserta.delete(timSekarang.treePeserta.root, poinPengirimAwal, idPengirim);
                timSekarang.treePeserta.root = timSekarang.treePeserta.delete(timSekarang.treePeserta.root, poinPenerimaAwal, idPenerima);
                
                // Insert nodes dengan poin terbaru
                timSekarang.treePeserta.root = timSekarang.treePeserta.insert(timSekarang.treePeserta.root, poinPengirimBaru, idPengirim);
                timSekarang.treePeserta.root = timSekarang.treePeserta.insert(timSekarang.treePeserta.root, poinPenerimaBaru, idPenerima);
                
                // Print updated points
                out.println(poinPengirimBaru + " " + poinPenerimaBaru);
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



            else if (query.equals("V")) {
                int peserta1ID = in.nextInt();
                int peserta2ID = in.nextInt();
                int timID = in.nextInt();
                int hasil = in.nextInt();
            
                Tim timSofita = LLTim.getSofitaSekarang();
                
                if (timSofita == null) {
                    out.println(-1);
                    continue;
                }
            
                // Find Tim with timID
                Tim timPeserta2 = LLTim.header;
                boolean timFound = false;
                for (int j = 0; j < LLTim.size; j++) {
                    timPeserta2 = timPeserta2.next;
                    if (timPeserta2.idTim == timID) {
                        timFound = true;
                        break;
                    }
                }
            
                if (!timFound) {
                    out.println(-1);
                    continue;
                }
            
                // Find peserta1 in Sofita's current team
                Node nodePeserta1 = timSofita.treePeserta.find(timSofita.treePeserta.root, peserta1ID);
                // Find peserta2 in specified team
                Node nodePeserta2 = timPeserta2.treePeserta.find(timPeserta2.treePeserta.root, peserta2ID);
            
                // Check if both peserta exist
                if (nodePeserta1 == null || nodePeserta2 == null) {
                    out.println(-1);
                    continue;
                }
            
                // Store current points
                long poin1 = nodePeserta1.key;
                long poin2 = nodePeserta2.key;
                long id1 = nodePeserta1.id;
                long id2 = nodePeserta2.id;
            
                // Remove peserta from their trees
                timSofita.treePeserta.root = timSofita.treePeserta.delete(timSofita.treePeserta.root, poin1, id1);
                timPeserta2.treePeserta.root = timPeserta2.treePeserta.delete(timPeserta2.treePeserta.root, poin2, id2);
            
                // Update points based on hasil
                if (hasil == 0) { // Draw
                    poin1 += 1;
                    poin2 += 1;
                    out.println(poin1 + " " + poin2);
                } else if (hasil == 1) { // Peserta1 wins
                    poin1 += 3;
                    poin2 -= 3;
                    out.println(poin1);
                } else if (hasil == -1) { // Peserta2 wins
                    poin2 += 3;
                    poin1 -= 3;
                    out.println(poin2);
                }
            
                // Reinsert with updated points
                timSofita.treePeserta.root = timSofita.treePeserta.insert(timSofita.treePeserta.root, poin1, id1);
                timPeserta2.treePeserta.root = timPeserta2.treePeserta.insert(timPeserta2.treePeserta.root, poin2, id2);
            
                // Update team scores
                timSofita.updateTeamScore();
                timPeserta2.updateTeamScore();
            
                // Check if any team should be eliminated
                
                if (timSofita.sumScore <= 0) {
                    LLTim.deleteTim(timSofita);
                    // Move Sofita to the team with highest score
                    Tim highestScoreTeam = LLTim.findHighestScoreTeam();
                    if (highestScoreTeam != null) {
                        LLTim.setsofitaSekarang(highestScoreTeam);
                    }
                }
            
                if (timPeserta2.sumScore <= 0) {
                    LLTim.deleteTim(timPeserta2);
                    // If joki's team is eliminated, move joki to the team with lowest score
                    if (LLTim.getPenjokiNow() == timPeserta2) {
                        Tim lowestScoreTeam = LLTim.findLowestScoreTeam();
                        if (lowestScoreTeam != null) {
                            LLTim.setPenjokiNow(lowestScoreTeam);
                        } else {
                            LLTim.setPenjokiNow(null);
                        }
                    }
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
            // this.jumlahPeserta = 0;
        }

        void updateTeamScore() {
            long newScore = 0;
            // Traverse through the AVL tree to sum up all peserta scores
            updateTeamScoreHelper(treePeserta.root, newScore);
            this.sumScore = newScore;
        }

        private void updateTeamScoreHelper(Node node, long score) {
            if (node == null) return;
            score += node.key; // assuming key is the poin
            updateTeamScoreHelper(node.left, score);
            updateTeamScoreHelper(node.right, score);
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

        public int getJumlahMatch(){
            return jumlahMatch;
        }

        public int getPoin(){
            return poinPeserta;
        }

    }

    // Class DaftarTim digunakan untuk menyimpan semua Tim
    static class CircDoublyLL<E> {
        int size; // Jumlah Tim
        Tim header, footer;
        Tim sofitaSekarang; // untuk menyimpan lokasi Sofita
        Tim jokiSaiki; // posisi joki

        // construct empty list
        CircDoublyLL() {
            this.size = 0;
            this.header = new Tim(null, 0, 0);
            this.footer = new Tim(null, 0, 0);
        }

        Tim getLast(){
            return footer.prev;
        }

        Tim getFirst() {
            return header.next;
        }

        // Method digunakan untuk menambahkan node baru di akhir linkedlist
        void addLast(Tim tim) {
            if (this.size == 0) { // empty
                footer.prev = tim;
                tim.next = footer;
                header.next = tim;
                tim.prev = header;

            } else { 
                footer.prev.next = tim;
                tim.prev = footer.prev;
                tim.next = footer;
                footer.prev = tim;
            }

            this.size += 1;
        }


        /*
         * UNTUK QUERY G
         */

         void addFirst(Tim tim) {
            if (size == 0) {
                // Sama seperti `addLast` untuk linked list kosong
                addLast(tim);
            } else {
                // Sisipkan di depan header.next
                tim.next = header.next;
                tim.prev = header;
                header.next.prev = tim;
                header.next = tim;
                size++;
            }
        }

        void addBefore(Tim existingTim, Tim newTim) {
            if (existingTim == header || size == 0) {
                throw new IllegalArgumentException("Cannot add before header or in an empty list.");
            }
        
            newTim.prev = existingTim.prev;
            newTim.next = existingTim;
            existingTim.prev.next = newTim;
            existingTim.prev = newTim;
            size++;
        }

        void addAfter(Tim existingTim, Tim newTim) {
            if (existingTim == footer || size == 0) {
                throw new IllegalArgumentException("Cannot add after footer or in an empty list.");
            }
        
            newTim.next = existingTim.next;
            newTim.prev = existingTim;
            existingTim.next.prev = newTim;
            existingTim.next = newTim;
            size++;
        }


        void collectUniquePoints(Node node, HashSet<Long> uniquePoints) {
            if (node == null) {
                return;
            }
            
            // Add current node's point value to HashSet
            uniquePoints.add((long)node.key);  // Cast key to long if necessary
            
            // Recursively process left and right subtrees
            collectUniquePoints(node.left, uniquePoints);
            collectUniquePoints(node.right, uniquePoints);
        }

        // Method digunakan untuk remove node Tim dari linkedlist
        Tim remove(Tim Tim) {
            if (this.size == 0) { // empty
                // do nothing
                throw new NullPointerException("LinkedList Size == 0");
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


        void deleteTim(Tim tim) {
            if (tim == header || size == 0) return;
            
            tim.prev.next = tim.next;
            tim.next.prev = tim.prev;
            size--;
        }


        /*
            INI UNTUK QUERY M
         */ 
        void setAllPointsToOne(Tim tim){
            AVLTree treeBaruLagi = new AVLTree();
            long sumPeserta = tim.jumlahPeserta;
            for(int i = 0; i < sumPeserta; i++){
                treeBaruLagi.root = treeBaruLagi.insert(treeBaruLagi.root, 1, i);
            }
        }

        void deletePesertaTopThree(Tim tim){
            AVLTree tree = tim.treePeserta;
            for (int i = 0; i<3 && tree.root != null; i++){
                Node maxNode = tree.findMax();
                tree.root = tree.delete(tree.root, maxNode.key, maxNode.key);
                tim.jumlahPeserta--;
                tim.sumScore -= maxNode.key;
            }
        }

        Tim findTimLowestScoreM(){
            Tim current = LLTim.header.next;
            Tim lowestTim = current;
            long lowestSkor = current.sumScore;
            while(current != LLTim.footer){
                if(current.sumScore<lowestSkor){
                    lowestSkor = current.sumScore;
                    lowestTim = current;
                }
                current = current.next;
            }
            return lowestTim;
        }

        Tim findTimHighestScoreM(){
            Tim current = LLTim.header.next;
            Tim highestTim = current;
            long highestSkor = current.sumScore;
            while(current != LLTim.footer){
                if(current.sumScore>highestSkor){
                    highestSkor = current.sumScore;
                    highestTim = current;
                }
                current = current.next;
            }
            return highestTim;
        }

        void ketemu(){
            Tim timJoki = getPenjokiNow();
            timJoki.sofiKetemuJoki++;
            Tim timSkorTerkecil = findTimLowestScoreM();
            setPenjokiNow(timSkorTerkecil);
            if(timJoki.sofiKetemuJoki == 1){
                deletePesertaTopThree(timJoki);
                if(timJoki.jumlahPeserta < 7){
                    LLTim.remove(timJoki);
                }
            } else if(timJoki.sofiKetemuJoki == 2){
                setAllPointsToOne(timJoki);
            } else if(timJoki.sofiKetemuJoki == 3){
                LLTim.remove(timJoki);
            }

            if (size == 0){
                return;
            }

            Tim timSkorTertinggi = findTimHighestScoreM();
            setsofitaSekarang(timSkorTertinggi);
        }


        /*
            INI UNTUK QUERY V
         */
         Tim findLowestScoringTeam(CircDoublyLL<Tim> LLTim, Tim excludeTeam) {
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

        Tim findHighestScoreTeam() {
            if (size == 0) return null;
            
            Tim current = header.next;
            Tim highest = current;
            for (int i = 0; i < size; i++) {
                if (current.sumScore > highest.sumScore) {
                    highest = current;
                }
                current = current.next;
            }
            return highest;
        }

        Tim findLowestScoreTeam() {
            if (size == 0) return null;
            
            Tim current = header.next;
            Tim lowest = current;
            for (int i = 0; i < size; i++) {
                if (current.sumScore < lowest.sumScore) {
                    lowest = current;
                }
                current = current.next;
            }
            return lowest;
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
            Long poinTerkecil = Long.MAX_VALUE;
            Long poinKeduaTerkecil = Long.MAX_VALUE;
            Tim timPoinTerkecil = null;
            Tim timPoinKeduaKecil = null;
        
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
                throw new NullPointerException("LinkedList Size == 0");
            } else if (this.size == 1) { 
            } else if (sofitaSekarang.next.equals(footer)) { // elemen terakhir
                sofitaSekarang = header.next;
            } else {
                sofitaSekarang = sofitaSekarang.next;
            }
            return sofitaSekarang;
        }

        // Menggerakan Sofita ke kiri
        Tim gerakKiriSofita() {
            if (this.size == 0) {
                throw new NullPointerException("LinkedList Size is == 0");
            } else if (this.size == 1) { 
            } else if (sofitaSekarang.prev.equals(header)) { // elemen pertama
                sofitaSekarang = footer.prev;
            } else {
                sofitaSekarang = sofitaSekarang.prev;
            }
            return sofitaSekarang;
        }


        // Menggerakan Sofita ke kanan
        Tim gerakKananPenjoki() {
            if (this.size == 0) { // empty
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
    long key, height, count; // key => poin, count => banyaknya node pada suatu subtree dengan root == node
    Node left, right;
    long jumlahSama; // jumlah isi key yg sama (duplicate)
    int jumlahCurang = 0;

    Peserta peserta;
    // long jumlahPertandingan; // Added jumlahPertandingan field
    int id;


    Node(long key, long id) {
        // this.peserta = peserta;
        // this.key = peserta.poinPeserta; // Set key based on points
        this.key = key;
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

    Node findNodeById(Node root, long targetId) {
        if (root == null) return null;
        
        // Traverse left subtree
        Node leftResult = findNodeById(root.left, targetId);
        if (leftResult != null) return leftResult;
        
        // Check current node's id
        if (root.id == targetId) return root;
        
        // Traverse right subtree
        return findNodeById(root.right, targetId);
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
        // Implement insert node to AVL Tree
        Node insert(Node node, long key, long id) {
            if (node == null) {
                return (new Node(key, id));
            }

            if (key < node.key) {
                node.left = insert(node.left, key, id);
            } else if (key > node.key) {
                node.right = insert(node.right, key, id);
            } else {
                node.jumlahSama += 1;
                node.count += 1;
                return node;

            }

            // Update height & count
            node.height = 1 + max(getHeight(node.left), getHeight(node.right));
            node.count = node.jumlahSama + getCount(node.left) + getCount(node.right);

            // Get balance factor
            long balance = getBalance(node);

            // If this node becomes unbalanced, then there are 4 cases

            // Left Left Case
            if (balance > 1 && key < node.left.key) {
                return rightRotate(node);
            }

            // Right Right Case
            if (balance < -1 && key > node.right.key) {
                return leftRotate(node);
            }

            // Left Right Case
            if (balance > 1 && key > node.left.key) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }

            // Right Left Case
            if (balance < -1 && key < node.right.key) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }

            return node;
        }

    Node delete(Node root, Peserta peserta) {
        return delete(root, peserta.poinPeserta, peserta.idPeserta);
    }

    Node find(Node root, long id) {
        if (root == null) return null;
        
        if (id < root.id) {
            return find(root.left, id);
        } else if (id > root.id) {
            return find(root.right, id);
        } else {
            return root;
        }
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