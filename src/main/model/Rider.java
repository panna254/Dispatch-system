package model;
public class Rider{
    private String name;
    private String id;
    private String pickupLocation;
   
  public Rider(String name, String id,String pickupLocation)
  {
    this.name = name;
    this.id = id;
     this.pickupLocation = pickupLocation;

  }
  //getter i think hehehe
  public String getName(){
    return name;
  }
  public String getId(){
    return id;
  }
  public String getPickupLocation(){
    return pickupLocation;
}
  
  //from my research i found out about toString that helps return Rider info without necessarily being Output
  @Override
  public String toString(){
    return "Rider Name:"+name+",\n ID number: " + id;
  }
}