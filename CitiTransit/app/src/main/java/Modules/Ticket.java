package Modules;

/**
 * Created by yianmo on 10/13/17.
 */

public class Ticket {

    private String ticketType;

    private double ticketPrice;

    public Ticket(String ticketType, double ticketPrice){
        this.ticketType = ticketType;
        this.ticketPrice = ticketPrice;
    }

    public String getTicketType(){
        return ticketType;
    }

    public double getTicketPrice(){
        return ticketPrice;
    }
}
