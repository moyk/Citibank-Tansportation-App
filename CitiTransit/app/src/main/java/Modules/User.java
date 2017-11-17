package Modules;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

/**
 * Created by nick on 11/5/17.
 */

@DynamoDBTable(tableName = "CitiGoUsers")
public class User {

    private String userId;
    private String userName;
    private String userEmail;
    private String presetStartPoint;
    private String presetDestination;

    public void setUserId(String userId){
        this.userId = userId;
    }

    @DynamoDBHashKey(attributeName = "FirebaseUserId")
    public String getUserId(){
        return this.userId;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    @DynamoDBAttribute(attributeName = "UserName")
    public String getUserName(){
        return this.userName;
    }

    public void setUserEmail(String userEmail){
        this.userEmail = userEmail;
    }

    @DynamoDBAttribute(attributeName = "UserEmail")
    public String getUserEmail(){
        return this.userEmail;
    }

    @DynamoDBAttribute(attributeName = "PresetStartPoint")
    public String getUserPresetStartPoint(){
        return this.presetStartPoint;
    }

    public void setUserPresetStartPoint(String presetStartPoint){
        this.presetStartPoint = presetStartPoint;
    }

    public void setPresetDestination(String presetDestination){
        this.presetDestination = presetDestination;
    }

    @DynamoDBAttribute(attributeName = "PresetDestination")
    public String getPresetDestination(){
        return this.presetDestination;
    }

}
