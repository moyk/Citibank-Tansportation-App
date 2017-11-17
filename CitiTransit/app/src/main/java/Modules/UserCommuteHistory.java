package Modules;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nick on 11/15/17.
 */

@DynamoDBTable(tableName = "CitiGoUserCommuteHitoryTable")
public class UserCommuteHistory implements Serializable{
    private String firebaseUserId;
    private String timeStamp;
    private String destination;
    private String startPoint;
    private double tripCost;
    private List<String> transitModes;
    private String commuteHistoryId;

    @DynamoDBHashKey(attributeName = "CommuteHistoryId")
    public String getCommuteHistoryId(){
        return this.commuteHistoryId;
    }

    public void setCommuteHistoryId(String commuteHistoryId){
        this.commuteHistoryId = commuteHistoryId;
    }

    public void setFirebaseUserId(String firebaseUserId){
        this.firebaseUserId = firebaseUserId;
    }

    @DynamoDBAttribute(attributeName = "FirebaseUserId")
    public String getFirebaseUserId(){
        return this.firebaseUserId;
    }

    public void setTimeStamp(String timeStamp){
        this.timeStamp = timeStamp;
    }

    @DynamoDBAttribute(attributeName = "TimeStamp")
    public String getTimeStamp(){
        return this.timeStamp;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }

    @DynamoDBAttribute(attributeName = "Destination")
    public String getDestination(){
        return this.destination;
    }

    public void setStartPoint(String startPoint){
        this.startPoint = startPoint;
    }

    @DynamoDBAttribute(attributeName = "StartPoint")
    public String getStartPoint(){
        return this.startPoint;
    }

    public void setTripCost(double tripCost){
        this.tripCost = tripCost;
    }

    @DynamoDBAttribute(attributeName = "TripCost")
    public double getTripCost(){
        return this.tripCost;
    }

    @DynamoDBAttribute(attributeName = "TransitModes")
    public List<String> getTransitModes(){
        return this.transitModes;
    }

    public void setTransitModes(List<String> transitModes){
        this.transitModes = transitModes;
    }
}
