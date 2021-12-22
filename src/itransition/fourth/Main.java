package src.itransition.fourth;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.security.SecureRandom;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;


class Hmac{
    private static final String HMAC_SHA256 = "HmacSHA256";
    static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    static String calculateHMAC(String data, byte [] key)
            throws InvalidKeyException, NoSuchAlgorithmException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, HMAC_SHA256);
        Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(secretKeySpec);
        return toHexString(mac.doFinal(data.getBytes()));
    }

    static String generate(ArrayList<String> ways) throws NoSuchAlgorithmException, InvalidKeyException {
        SecureRandom secureRandom = new SecureRandom();
        byte []bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        Random rand = new Random();
        String data = ways.get(rand.nextInt(ways.size()));
        return calculateHMAC(data, bytes);
    }
}


class Rules{
    static void determine_the_win(ArrayList<String> ways, int data_key, int user_num){
        int R = (ways.size() + data_key - user_num) % ways.size();
        if (R % 2 == 1) System.out.println("You lost!");
        else if (R % 2 == 0) System.out.println("You win!");
        else System.out.println("Draw!");
    }
}


class Table{
    private static final int elem_size = 12;
    static void generate(){
        String [] example_args = {"   ", "rock", "paper", "scissors", "lizard", "Spock"};
        String [] first_row = {"rock", "draw", "win", "win", "lose", "lose"};
        String [] second_row = {"paper", "lose", "draw", "win", "win", "lose"};
        String [] third_row = {"Spock", "lose", "lose", "draw", "win", "win"};
        String [] four_row = {"scissors", "win", "lose", "lose", "draw", "win"};
        String [] five_row = {"lizard", "win", "win", "lose", "lose", "draw"};
        int i = 0;
        for (String elem: example_args) {
            while (elem.length() < elem_size) {
                elem = elem.concat(" ");
            }
            example_args[i] = elem;
            i = i + 1;
        }
        i = 0;
        for (String elem: example_args) System.out.print(elem + " ");
        System.out.print("\n");
        for (String elem: first_row) {
            while (elem.length() < elem_size) {
                elem = elem.concat(" ");
            }
            first_row[i] = elem;
            i = i + 1;
        }
        i = 0;
        for (String elem: first_row) System.out.print(elem + " ");
        System.out.print("\n");
        for (String elem: second_row) {
            while (elem.length() < elem_size) {
                elem = elem.concat(" ");
            }
            second_row[i] = elem;
            i = i + 1;
        }
        i = 0;
        for (String elem: second_row) System.out.print(elem + " ");
        System.out.print("\n");
        for (String elem: third_row) {
            while (elem.length() < elem_size) {
                elem = elem.concat(" ");
            }
            third_row[i] = elem;
            i = i + 1;
        }
        i = 0;
        for (String elem: third_row) System.out.print(elem + " ");
        System.out.print("\n");
        for (String elem: four_row) {
            while (elem.length() < elem_size) {
                elem = elem.concat(" ");
            }
            four_row[i] = elem;
            i = i + 1;
        }
        i = 0;
        for (String elem: four_row) System.out.print(elem + " ");
        System.out.print("\n");
        for (String elem: five_row) {
            while (elem.length() < elem_size) {
                elem = elem.concat(" ");
            }
            five_row[i] = elem;
            i = i + 1;
        }
        for (String elem: five_row) System.out.print(elem + " ");
        System.out.print("\n");
    }
}



public class Main {
    static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }


    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException {
        if (args.length < 3){
            System.out.println("The number of arguments must be greater than 2!");
            return;
        }
        if (args.length % 2 == 0){
            System.out.println("The number of arguments must be odd!");
            return;
        }
        ArrayList<String> ways = new ArrayList<String>();
        Map<Integer, String> ways_map = new HashMap<Integer, String>();
        int i = 1;
        boolean first_out = true;
        for (String arg: args){
            ways.add(arg);
            if (ways_map.containsValue(arg)){
                System.out.println("There must not be identical elements among the arguments!");
                return;
            }
            ways_map.put(i, arg);
            i = i + 1;
        }
        while (true){
            Random rand = new Random();
            String data = ways.get(rand.nextInt(ways.size()));
            int data_key = 0;
            String hmac = Hmac.generate(ways);
            System.out.println("HMAC:");
            System.out.println(hmac);
            if (first_out) System.out.println("Available moves:");
            for (Integer key: ways_map.keySet()) {
                String value = ways_map.get(key);
                if (value.equals(data)){
                    data_key = key;
                }
                if (first_out) System.out.println(key + "-" + value);
            }
            if (first_out) {
                System.out.println("0-exit");
                System.out.println("?-help");
            }
            first_out = false;
            Scanner in = new Scanner(System.in);
            System.out.print("Enter your move: ");
            int user_num = 0;
            String num = in.next();
            if (isNumeric(num)){
                user_num = Integer.parseInt (num);
                if (user_num == 0){
                    break;
                }
                else if(user_num > ways.size()){
                    System.out.printf("Please enter number in [0-%d] or enter ? for help\n", ways.size());
                    continue;
                }
                System.out.printf("Your move: %s \n", ways_map.get(user_num));
                System.out.printf("Computer move: %s \n", data);
                Rules.determine_the_win(ways, data_key, user_num);
            }
            else if(num.equals("?")){
                Table.generate();
            }
            else{
                System.out.printf("Please enter number in [0-%d] or enter ? for help\n", ways.size());
            }
        }
    }
}
