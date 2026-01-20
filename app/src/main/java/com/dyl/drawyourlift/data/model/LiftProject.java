package com.dyl.drawyourlift.data.model;

public class LiftProject {

    // Step 0 (Home)
    public String doorType;
    public String liftType;

    // Step 1 – Project Details
    public String userName;
    public String projectName;
    public int passengerCapacity;
    public int numberOfFloors;
    public int shaftWidth;
    public int shaftDepth;
    public int floorHeight;
    public int pitDepth;
    public int overheadHeight;

    // Step 2 – Door Details (later)
    public String doorOperator;
    public String openingSide;
    public int clearOpening;
    public String doorFinish;
    public String doorVision;
    public int doorHeight;
    public String cabinFinish;

    // Step 3 – Machine (later)
    public String ropingStyle;
    public String machineBrand;
    public String machineModel;
    public String counterFrameSide;
    public int counterDbgSize;
    public int counterPulley;

    // Step 4 – Custom Dimensions (later)
    public int leftWallDistance;
    public int mainBracketDistance;
    public int counterBracketDistance;
    public String guideRailPreference;
    public int cabinWallGap;
    public int cabinCenterOffset;
    public String pdfType;
    public String siteAddress;
    public int bracketDistance;
}
