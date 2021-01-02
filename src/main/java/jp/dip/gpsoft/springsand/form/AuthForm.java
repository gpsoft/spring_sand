package jp.dip.gpsoft.springsand.form;

public class AuthForm {
	private String userid;
	private String passwd;

	public AuthForm() {
		userid = "";
		passwd = "";
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
}
