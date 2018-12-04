package Demo;

import com.intersystems.xep.annotations.Id;

public class Airport {
	@Id(generated=false)
	private String code;
	private String name;
	private Demo.Location location;

	public Airport() {
		
	}

	public Airport(String name, String code, Location location) {
		this.name = name;
		this.code = code;
		this.location = location;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Demo.Location getLocation() {
		return location;
	}
	public void setLocation(Demo.Location location) {
		this.location = location;
	}
}
