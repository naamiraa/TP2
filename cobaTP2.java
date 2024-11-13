import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class cobaTP2 {
    private static InputReader in;
    private static PrintWriter out;
    private static List<Team> teams;
    private static int sofitaIndex = 0; // index of the team Sofita is currently monitoring
    private static int lastTeamId;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int m = in.nextInteger(); // Number of initial teams
        teams = new ArrayList<>();
        lastTeamId = m;

        // Initialize each team and add participants
        for (int i = 1; i <= m; i++) {
            int participantCount = in.nextInteger();
            Team team = new Team(i);
            team.addParticipants(participantCount);
            teams.add(team);
            out.println(participantCount);
        }

        int o = in.nextInteger(); // Number of commands
        for (int i = 0; i < o; i++) {
            String command = in.next();
            switch (command) {
                case "A":
                    int count = in.nextInteger();
                    addParticipants(count);
                    break;
                case "E":
                    int thresholdPoints = in.nextInteger();
                    eliminateTeams(thresholdPoints);
                    break;
                case "U":
                    countUniquePoints();
                    break;
                case "G":
                    String position = in.next();
                    generateNewTeam(position);
                    break;
                case "V":
                    int id1 = in.nextInteger();
                    int id2 = in.nextInteger();
                    int teamId = in.nextInteger();
                    int result = in.nextInteger();
                    conductMatch(id1, id2, teamId, result);
                    break;
                case "J":
                    String direction = in.next();
                    jumpSofita(direction);
                    break;
                default:
                    out.println("Invalid command");
            }
        }

        out.flush();
        out.close();
    }


    // A: Cetak jumlah peserta pada tim setelah ditambahkan peserta baru
    private static void addParticipants(int count) {
        Team currentTeam = teams.get(sofitaIndex);
        currentTeam.addParticipants(count);
        out.println(currentTeam.getParticipantCount());
    }


    // B: Cetak jumlah peserta yang memenuhi syarat yang ditentukan Sofita
    // Hitung outlier
        public void calculateOutliers(char extremeType) {
        List<Integer> sortedPoints = sofitaTeam.participants.stream()
                .map(p -> p.points)
                .sorted()
                .collect(Collectors.toList());
        
        int q1Index = (int)Math.floor(0.25 * (sortedPoints.size() - 1));
        int q3Index = (int)Math.floor(0.75 * (sortedPoints.size() - 1));
        int q1 = sortedPoints.get(q1Index);
        int q3 = sortedPoints.get(q3Index);
        int iqr = q3 - q1;
        int L = q1 - (int)(1.5 * iqr);
        int U = q3 + (int)(1.5 * iqr);
        
        long count;
        if (extremeType == 'U') {
            count = sofitaTeam.participants.stream().filter(p -> p.points > U).count();
        } else {
            count = sofitaTeam.participants.stream().filter(p -> p.points < L).count();
        }
        System.out.println(count);
    }


    // M: Cetak ID tim yang diawasi Sofita setelah pindah
    public void moveSofita(char direction) {
        int index = teams.indexOf(sofitaTeam);
        if (direction == 'L') {
            index = (index - 1 + teams.size()) % teams.size();
        } else {
            index = (index + 1) % teams.size();
        }
        sofitaTeam = teams.get(index);
        System.out.println(sofitaTeam.id);
    }




    // T: Cetak poin peserta pengirim dan peserta penerima (dipisah dengan whitespace)
    // T: Cetak -1 apabila salah satu atau kedua ID peserta tidak ditemukan
    // T: Cetak -1 apabila jumlah poin yang dikirim < jumlah poin pengirim
    public void transferPoints(int senderId, int receiverId, int points) {
        Participant sender = sofitaTeam.participants.stream().filter(p -> p.id == senderId).findFirst().orElse(null);
        Participant receiver = sofitaTeam.participants.stream().filter(p -> p.id == receiverId).findFirst().orElse(null);
        
        if (sender == null || receiver == null || sender.points < points) {
            System.out.println(-1);
            return;
        }
        
        sender.points -= points;
        receiver.points += points;
        System.out.println(sender.points + " " + receiver.points);
    }
    


    // G: Cetak ID tim yang baru saja berpartisipasi
    private static void generateNewTeam(String position) {
        Team newTeam = new Team(++lastTeamId);
        newTeam.addParticipants(7);
        
        if (position.equals("L")) {
            teams.add(sofitaIndex, newTeam);
        } else {
            teams.add(sofitaIndex + 1, newTeam);
        }
        
        out.println(newTeam.getId());
    }




    // V: Cetak poin peserta yang menang
    // V: Jika seri, cetak poin dari kedua peserta (dipisah dengan whitespace)
    public void commandV(int id1, int id2, int teamId, int result) {
        Team currentTeam = teams.get(sofitaIndex);
        Participant participant1 = findParticipantById(currentTeam, id1);
        Team otherTeam = findTeamById(teamId);
        Participant participant2 = findParticipantById(otherTeam, id2);

        if (participant1 == null || participant2 == null) {
            System.out.println("-1");
            return;
        }
        
        if (result == 0) {
            participant1.points += 1;
            participant2.points += 1;
            System.out.println(participant1.points + " " + participant2.points);
        } else if (result == 1) {
            participant1.points += 3;
            participant2.points -= 3;
            System.out.println(participant1.points);
        } else if (result == -1) {
            participant2.points += 3;
            participant1.points -= 3;
            System.out.println(participant2.points);
        }
    }


    // E: Cetak jumlah tim yang terkena eliminasi
    private static void eliminateTeams(int thresholdPoints) {
        int eliminatedCount = 0;
        Iterator<Team> iterator = teams.iterator();
        while (iterator.hasNext()) {
            Team team = iterator.next();
            if (team.getTotalPoints() < thresholdPoints) {
                iterator.remove();
                eliminatedCount++;
                if (sofitaIndex >= teams.size()) {
                    sofitaIndex = 0;
                }
            }
        }
        out.println(eliminatedCount);
    }

    // U: Cetak Jumlah peserta yang memiliki nilai unik dalam tim yang diawasi sofita. 
    private static void countUniquePoints() {
        Team currentTeam = teams.get(sofitaIndex);
        out.println(currentTeam.countUniquePoints());
    }
    


    // R: Cetak ID tim dari tempat Sofita mengawas. 
    public void sortTeams() {
        sortedTeams = new TreeSet<>((t1, t2) -> {
            int comp = Integer.compare(t2.getTotalPoints(), t1.getTotalPoints());
            if (comp == 0) comp = Integer.compare(t2.participants.size(), t1.participants.size());
            if (comp == 0) comp = Integer.compare(t1.id, t2.id);
            return comp;
        });
        
        sortedTeams.addAll(teams);
        sofitaTeam = sortedTeams.first();
        System.out.println(sofitaTeam.id);
    }

    // J: Cetak ID tim dari tempat penjoki memberikan layanannya. 
    public void commandJ(String direction) {
        sofitaIndex = (direction.equals("L")) ? (sofitaIndex - 1 + teams.size()) % teams.size() : (sofitaIndex + 1) % teams.size();
        System.out.println(teams.get(sofitaIndex).id);
    }


    private static void conductMatch(int id1, int id2, int teamId, int result) {
        Team currentTeam = teams.get(sofitaIndex);
        Participant participant1 = currentTeam.findParticipantById(id1);
        Team otherTeam = findTeamById(teamId);
        Participant participant2 = (otherTeam != null) ? otherTeam.findParticipantById(id2) : null;

        if (participant1 == null || participant2 == null) {
            out.println("-1");
            return;
        }

        if (result == 0) {
            participant1.incrementPoints(1);
            participant2.incrementPoints(1);
            out.println(participant1.getPoints() + " " + participant2.getPoints());
        } else if (result == 1) {
            participant1.incrementPoints(3);
            participant2.incrementPoints(-3);
            out.println(participant1.getPoints());
        } else if (result == -1) {
            participant2.incrementPoints(3);
            participant1.incrementPoints(-3);
            out.println(participant2.getPoints());
        }
    }

    private static void jumpSofita(String direction) {
        if (direction.equals("L")) {
            sofitaIndex = (sofitaIndex - 1 + teams.size()) % teams.size();
        } else {
            sofitaIndex = (sofitaIndex + 1) % teams.size();
        }
        out.println(teams.get(sofitaIndex).getId());
    }

    private static Team findTeamById(int id) {
        for (Team t : teams) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    // Helper Classes
    static class Team {
        private int id;
        private LinkedList<Participant> participants;
        private int totalPoints;

        public Team(int id) {
            this.id = id;
            this.participants = new LinkedList<>();
            this.totalPoints = 0;
        }

        public int getId() {
            return id;
        }

        public void addParticipants(int count) {
            int startId = participants.isEmpty() ? 1 : participants.getLast().getId() + 1;
            for (int i = 0; i < count; i++) {
                Participant newParticipant = new Participant(startId + i, 3);
                participants.add(newParticipant);
                totalPoints += 3;
            }
        }

        public int getParticipantCount() {
            return participants.size();
        }

        public int getTotalPoints() {
            return totalPoints;
        }

        public int countUniquePoints() {
            Set<Integer> uniquePoints = new HashSet<>();
            for (Participant p : participants) {
                uniquePoints.add(p.getPoints());
            }
            return uniquePoints.size();
        }

        public Participant findParticipantById(int id) {
            for (Participant p : participants) {
                if (p.getId() == id) return p;
            }
            return null;
        }
    }

    static class Participant {
        private int id;
        private int points;

        public Participant(int id, int points) {
            this.id = id;
            this.points = points;
        }

        public int getId() {
            return id;
        }

        public int getPoints() {
            return points;
        }

        public void incrementPoints(int increment) {
            points += increment;
        }
    }

    // Efficient InputReader and Output with PrintWriter
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
