package com.team2.model;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"sitename",
"username",
"firstname",
"lastname",
"fullname",
"lang",
"userid",
"siteurl",
"userpictureurl",
"downloadfiles",
"uploadfiles",
"release",
"version",
"mobilecssurl",
"usercanmanageownfiles",
"userquota",
"usermaxuploadfilesize",
"userhomepage",
"userprivateaccesskey",
"siteid",
"sitecalendartype",
"usercalendartype",
"userissiteadmin",
"theme"
})
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("jsonschema2pojo")
public class UetAuthInfo {

	@JsonProperty("sitename")
	private String sitename;
	@JsonProperty("username")
	private String username;
	@JsonProperty("firstname")
	private String firstname;
	@JsonProperty("lastname")
	private String lastname;
	@JsonProperty("fullname")
	private String fullname;
	@JsonProperty("lang")
	private String lang;
	@JsonProperty("userid")
	private Integer userid;
	@JsonProperty("siteurl")
	private String siteurl;
	@JsonProperty("userpictureurl")
	private String userpictureurl;
	@JsonProperty("downloadfiles")
	private Integer downloadfiles;
	@JsonProperty("uploadfiles")
	private Integer uploadfiles;
	@JsonProperty("release")
	private String release;
	@JsonProperty("version")
	private String version;
	@JsonProperty("mobilecssurl")
	private String mobilecssurl;
	@JsonProperty("usercanmanageownfiles")
	private Boolean usercanmanageownfiles;
	@JsonProperty("userquota")
	private Integer userquota;
	@JsonProperty("usermaxuploadfilesize")
	private Integer usermaxuploadfilesize;
	@JsonProperty("userhomepage")
	private Integer userhomepage;
	@JsonProperty("userprivateaccesskey")
	private String userprivateaccesskey;
	@JsonProperty("siteid")
	private Integer siteid;
	@JsonProperty("sitecalendartype")
	private String sitecalendartype;
	@JsonProperty("usercalendartype")
	private String usercalendartype;
	@JsonProperty("userissiteadmin")
	private Boolean userissiteadmin;
	@JsonProperty("theme")
	private String theme;
	@JsonProperty("sitename")
	public String getSitename() {
	return sitename;
	}

	@JsonProperty("sitename")
	public void setSitename(String sitename) {
	this.sitename = sitename;
	}

	@JsonProperty("username")
	public String getUsername() {
	return username;
	}

	@JsonProperty("username")
	public void setUsername(String username) {
	this.username = username;
	}

	@JsonProperty("firstname")
	public String getFirstname() {
	return firstname;
	}

	@JsonProperty("firstname")
	public void setFirstname(String firstname) {
	this.firstname = firstname;
	}

	@JsonProperty("lastname")
	public String getLastname() {
	return lastname;
	}

	@JsonProperty("lastname")
	public void setLastname(String lastname) {
	this.lastname = lastname;
	}

	@JsonProperty("fullname")
	public String getFullname() {
	return fullname;
	}

	@JsonProperty("fullname")
	public void setFullname(String fullname) {
	this.fullname = fullname;
	}

	@JsonProperty("lang")
	public String getLang() {
	return lang;
	}

	@JsonProperty("lang")
	public void setLang(String lang) {
	this.lang = lang;
	}

	@JsonProperty("userid")
	public Integer getUserid() {
	return userid;
	}

	@JsonProperty("userid")
	public void setUserid(Integer userid) {
	this.userid = userid;
	}

	@JsonProperty("siteurl")
	public String getSiteurl() {
	return siteurl;
	}

	@JsonProperty("siteurl")
	public void setSiteurl(String siteurl) {
	this.siteurl = siteurl;
	}

	@JsonProperty("userpictureurl")
	public String getUserpictureurl() {
	return userpictureurl;
	}

	@JsonProperty("userpictureurl")
	public void setUserpictureurl(String userpictureurl) {
	this.userpictureurl = userpictureurl;
	}

	@JsonProperty("downloadfiles")
	public Integer getDownloadfiles() {
	return downloadfiles;
	}

	@JsonProperty("downloadfiles")
	public void setDownloadfiles(Integer downloadfiles) {
	this.downloadfiles = downloadfiles;
	}

	@JsonProperty("uploadfiles")
	public Integer getUploadfiles() {
	return uploadfiles;
	}

	@JsonProperty("uploadfiles")
	public void setUploadfiles(Integer uploadfiles) {
	this.uploadfiles = uploadfiles;
	}

	@JsonProperty("release")
	public String getRelease() {
	return release;
	}

	@JsonProperty("release")
	public void setRelease(String release) {
	this.release = release;
	}

	@JsonProperty("version")
	public String getVersion() {
	return version;
	}

	@JsonProperty("version")
	public void setVersion(String version) {
	this.version = version;
	}

	@JsonProperty("mobilecssurl")
	public String getMobilecssurl() {
	return mobilecssurl;
	}

	@JsonProperty("mobilecssurl")
	public void setMobilecssurl(String mobilecssurl) {
	this.mobilecssurl = mobilecssurl;
	}

	@JsonProperty("usercanmanageownfiles")
	public Boolean getUsercanmanageownfiles() {
	return usercanmanageownfiles;
	}

	@JsonProperty("usercanmanageownfiles")
	public void setUsercanmanageownfiles(Boolean usercanmanageownfiles) {
	this.usercanmanageownfiles = usercanmanageownfiles;
	}

	@JsonProperty("userquota")
	public Integer getUserquota() {
	return userquota;
	}

	@JsonProperty("userquota")
	public void setUserquota(Integer userquota) {
	this.userquota = userquota;
	}

	@JsonProperty("usermaxuploadfilesize")
	public Integer getUsermaxuploadfilesize() {
	return usermaxuploadfilesize;
	}

	@JsonProperty("usermaxuploadfilesize")
	public void setUsermaxuploadfilesize(Integer usermaxuploadfilesize) {
	this.usermaxuploadfilesize = usermaxuploadfilesize;
	}

	@JsonProperty("userhomepage")
	public Integer getUserhomepage() {
	return userhomepage;
	}

	@JsonProperty("userhomepage")
	public void setUserhomepage(Integer userhomepage) {
	this.userhomepage = userhomepage;
	}

	@JsonProperty("userprivateaccesskey")
	public String getUserprivateaccesskey() {
	return userprivateaccesskey;
	}

	@JsonProperty("userprivateaccesskey")
	public void setUserprivateaccesskey(String userprivateaccesskey) {
	this.userprivateaccesskey = userprivateaccesskey;
	}

	@JsonProperty("siteid")
	public Integer getSiteid() {
	return siteid;
	}

	@JsonProperty("siteid")
	public void setSiteid(Integer siteid) {
	this.siteid = siteid;
	}

	@JsonProperty("sitecalendartype")
	public String getSitecalendartype() {
	return sitecalendartype;
	}

	@JsonProperty("sitecalendartype")
	public void setSitecalendartype(String sitecalendartype) {
	this.sitecalendartype = sitecalendartype;
	}

	@JsonProperty("usercalendartype")
	public String getUsercalendartype() {
	return usercalendartype;
	}

	@JsonProperty("usercalendartype")
	public void setUsercalendartype(String usercalendartype) {
	this.usercalendartype = usercalendartype;
	}

	@JsonProperty("userissiteadmin")
	public Boolean getUserissiteadmin() {
	return userissiteadmin;
	}

	@JsonProperty("userissiteadmin")
	public void setUserissiteadmin(Boolean userissiteadmin) {
	this.userissiteadmin = userissiteadmin;
	}

	@JsonProperty("theme")
	public String getTheme() {
	return theme;
	}

	@JsonProperty("theme")
	public void setTheme(String theme) {
	this.theme = theme;
	}

	@Override
	public String toString() {
		return "UetAuthInfo [sitename=" + sitename + ", username=" + username + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", fullname=" + fullname + ", lang=" + lang + ", userid=" + userid
				+ ", siteurl=" + siteurl + ", userpictureurl=" + userpictureurl + ", downloadfiles=" + downloadfiles
				+ ", uploadfiles=" + uploadfiles + ", release=" + release + ", version=" + version + ", mobilecssurl="
				+ mobilecssurl + ", usercanmanageownfiles=" + usercanmanageownfiles + ", userquota=" + userquota
				+ ", usermaxuploadfilesize=" + usermaxuploadfilesize + ", userhomepage=" + userhomepage
				+ ", userprivateaccesskey=" + userprivateaccesskey + ", siteid=" + siteid + ", sitecalendartype="
				+ sitecalendartype + ", usercalendartype=" + usercalendartype + ", userissiteadmin=" + userissiteadmin
				+ ", theme=" + theme + "]";
	}

	
}
