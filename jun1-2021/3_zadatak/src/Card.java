public class Card {

    private int card_number;
    private int card_type;

    public Card(int card_number, int card_type) {
        this.card_number = card_number;
        this.card_type = card_type;
    }

    @Override
    public String toString() {

        String card_type_name = "";
        if (this.card_type == 0)
            card_type_name = "pik";
        else if (this.card_type == 1)
            card_type_name = "herc";
        else if (this.card_type == 2)
            card_type_name = "tref";
        else if (this.card_type == 3)
            card_type_name = "karo";
        else {
            System.out.println("Something went wrong");
            System.exit(1);
        }

        return this.card_number + "." + card_type_name;
    }
}


