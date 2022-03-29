package seedu.duke;

import seedu.duke.CommandParsers.*;
import util.exceptions.NullException;
import util.exceptions.WrongCommandException;

import java.util.HashMap;
import java.util.Scanner;

public class UserInterface {
    private Warehouse warehouse;

    private ListParser listParser;
    private ViewParser viewParser;
    private AddParser addParser;
    private RemoveParser removeParser;
    private TotalParser totalParser;


    public UserInterface(Warehouse warehouse) {
        this.warehouse = warehouse;
        this.listParser = new ListParser(warehouse);
    }

    public void run() {
        Scanner input = new Scanner(System.in);
        String userInput = input.nextLine();


        // setting Warehouse capacity
        /*
        boolean isSet = false;
        do {
            Regex capacity = new Regex(userInput, "(?<cap>\\d*)");
            isSet = warehouse.setCapacity(capacity.getGroupValues().get("cap"));
            userInput = input.nextLine();
        } while (!isSet);
        */

        while (!userInput.equals("bye")) {
            // current implementation is just take 1st value for command
            try {
                String command = userInput.split(" ")[0];
                switch (command) {
                case "view":
                    //using flags here to distinguish between different views????
                    viewParser.parse(userInput);
                    break;
                case "list":
                    listParser.parse(userInput);
                    break;
                case "add":
                    addParser.parse(userInput);
                    break;
                case "remove":
                    removeParser.parse(userInput);
                    break;
                case "total":
                    totalParser.parse(userInput);
                    break;
                case "help":
                    displayHelp();
                    break;
                case "storage-capacity":
                    userInputIsStorageCapacity();
                    break;
                default:
                    //error exception here
                    throw new WrongCommandException("", false);
                }
            } catch (WrongCommandException wrongCommandException) {
                if (wrongCommandException.isCommand()) {
                    String wrongCommand = wrongCommandException.getCommand();
                    System.out.printf("%s command was used wrongly. Type help to see examples\n",
                            wrongCommand);
                    displayHelp();
                } else {
                    System.out.println("No such command. Type help to see examples");
                    displayHelp();
                }
            } catch (NullException nullException) {
                //catch null exception here
                System.out.println("Please enter the command again.");
            }
            System.out.println("Another command?");
            userInput = input.nextLine();
        }
    }

    private void userInputIsStorageCapacity() throws NullException {
        int totalGoods = warehouse.totalGoods();
        int warehouseCapacity = warehouse.getCapacity();
        Commands.storageCapacity(totalGoods, warehouseCapacity);
    }

    private void displayHelp() {
        Commands.help();
    }

    private void userInputIsTotal(String userInput) throws NullException, WrongCommandException {
        String regex = "(?<flag>[og])/ id/(?<id>\\d*)";
        Regex regexMatch = new Regex(userInput, regex);
        HashMap<String, String> matches = regexMatch.getGroupValues();
        if (matches.get("flag").equals("o")) {
            // get total orders with flag "o/"
            int totalOrders = warehouse.totalOrder();
            System.out.printf("There are %d goods in total.\n", totalOrders);
        } else if (matches.get("flag").equals("g")) {
            // get total goods with flag "g/"
            warehouse.totalGoods();
        } else {
            // wrong command exception
            throw new WrongCommandException("total", true);
        }
    }

    private void userInputIsRemove(String userInput) throws WrongCommandException {
        String regex = "(?<flag>[og])/ id/(?<id>\\d*)";
        Regex regexMatch = new Regex(userInput, regex);
        HashMap<String, String> matches = regexMatch.getGroupValues();

        if (matches.get("flag").equals("o")) {
            warehouse.removeOrder(matches.get("id"));
        } else if (matches.get("flag").equals("g")) {
            String regexGood = "id/(?<id>\\d*) q/(?<qty>\\d*)";
            HashMap<String,String> regexGoodMatch = new
                    Regex(userInput, regexGood).getGroupValues();
            warehouse.removeGoods(regexGoodMatch.get("id"), regexGoodMatch.get("qty"));
        } else {
            throw new WrongCommandException("remove", true);
        }
    }

    private void userInputIsAdd(String userInput) throws WrongCommandException {
        String regex = "(?<flag>[og])/";
        Regex regexMatch = new Regex(userInput, regex);
        HashMap<String, String> matches = regexMatch.getGroupValues();

        if (matches.get("flag").equals("o")) {
            String regexOrder = "oid/(?<oid>\\d*) r/(?<recv>.*) a/(?<address>.*)";
            HashMap<String,String> regexOrderMatches = new
                    Regex(userInput, regexOrder).getGroupValues();
            warehouse.addOrder(regexOrderMatches.get("oid"), regexOrderMatches.get("recv"),
                    regexOrderMatches.get("address"));
        } else if (matches.get("flag").equals("g")) {
            String regexGood = "oid/(?<oid>\\d*) gid/(?<gid>\\d*)"
                    + " n/(?<name>.*) q/(?<qty>\\d*) d/(?<desc>.*)";
            HashMap<String,String> regexGoodMatch = new
                    Regex(userInput, regexGood).getGroupValues();
            warehouse.addGoods(regexGoodMatch.get("oid"), regexGoodMatch.get("gid"),
                    regexGoodMatch.get("name"), regexGoodMatch.get("qty"), regexGoodMatch.get("desc"));
        } else {
            throw new WrongCommandException("add", true);
        }
    }

    private void userInputIsList(String userInput) throws WrongCommandException {
        HashMap<String, String> matches;
        Regex regexMatch;
        String regex;
        regex = "(?<flag>[og])/";
        regexMatch = new Regex(userInput, regex);
        matches = regexMatch.getGroupValues();

        if (matches.get("flag").equals("o")) {
            // list orders with flag "o/"
            warehouse.listOrders();
        } else if (matches.get("flag").equals("g")) {
            // list goods with flag "g/"
            warehouse.listGoods();
        } else {
            // wrong command exception
            throw new WrongCommandException("list", true);
        }
    }

    private void userInputIsView(String userInput) throws WrongCommandException {
        String regex = "(?<flag>[og])/ id/(?<id>\\d*)";
        Regex regexMatch = new Regex(userInput, regex);
        HashMap<String, String> matches = regexMatch.getGroupValues();
        if (matches.get("flag").equals("o")) {
            // view order with flag "o/"
            warehouse.viewOrder(matches.get("id"));
        } else if (matches.get("flag").equals("g")) {
            // view good with flag "g/"
            warehouse.viewGood(matches.get("id"));
        } else {
            // wrong command exception
            throw new WrongCommandException("view", true);
        }
    }
}
