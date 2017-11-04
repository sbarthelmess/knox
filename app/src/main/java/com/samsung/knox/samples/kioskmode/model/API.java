package com.samsung.knox.samples.kioskmode.model;

//The enum specifies if the item in list is a SETTER, GETTER, OPERATION
// or UI

/*
 * This class defines a type for each item in the list of APIs
 */

public class API {
	public String name;
	public String extraInfo;
	public APIType apiType;
	public boolean isContainerReqd;
	public boolean isCloningReqd;
	public KnoxSDKVersion knoxSdkVersionReqd;
	public SAFESDKVersion safeSdkVersionReqd;

	public API(String name, APIType apiType, boolean isContainerReqd, KnoxSDKVersion knoxSdkVersionReqd, String extraInfo) {
		this.name = name;
		this.apiType = apiType;
		this.isContainerReqd = isContainerReqd;
		this.knoxSdkVersionReqd = knoxSdkVersionReqd;
		this.extraInfo = extraInfo;
	}

	public API(String name, APIType apiType, boolean isContainerReqd, SAFESDKVersion safeSdkVersionReqd, String extraInfo) {
		this.name = name;
		this.apiType = apiType;
		this.isContainerReqd = isContainerReqd;
		this.safeSdkVersionReqd = safeSdkVersionReqd;
		this.extraInfo = extraInfo;
	}

}