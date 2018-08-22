package requirement1.models;

public enum EState {
	
	CA("California (CA)", 95, 245),
	NV("Nevada (NV)", 125, 205),
	OR("Oregon (OR)", 100,110),
	WA("Washington (WA)", 110,35),
	ID("Idaho (ID)", 170, 130),
	UT("Utah (UT)", 200,220),
	AZ("Arizona (AZ)", 190,280),
	NM("New Mexico (NM)", 260,290),
	CO("Colorado (CO)", 260,231),
	WY("Wyoming (WY)", 260,162),
	MT("Montata (MT)", 260,90),
	TX("Texas (TX)", 350,350),
	HI("Hawaii (HI)", 70,400),
	AK("Alaska (AK)", 170,410),
	OK("Oklahoma (OK)", 390,290),
	KS("Kansas (KS)", 370,235),
	NE("Nebraska (NE)", 340,180),
	SD("South Dakota (SD)", 340,130),
	ND("North Dakota (ND)", 350,70),
	MN("Minnesota (MN)", 435,90),
	IA("lowa (IA)", 425,170),
	MO("Missouri (MO)", 445,220),
	AR("Arkansas (AR)", 455,311),
	LA("Louisiana (LA)", 455,365),
	MS("Mississippi (MS)", 495,345),
	AL("Alabama (AL)", 535,340),
	GA("Georgia (GA)", 585,340),
	FL("Florida (FL)", 625,410),
	SC("South Carolina (SC)", 615,300),
	NC("North Carolina (NC)", 665,265),
	TN("Tennessee (TN)", 515,275),
	VA("Virginia (VA)", 650,220),
	WV("West Virginia (WV)",605,210),
	KY("Kentucky (KY)", 565,235),
	IL("Illinois (IL)", 495,225),
	IN("Indiana (IN)", 535,215),
	OH("Ohio (OH)", 575,200),
	PA("Pennsylvania (PA)", 655,170),
	WI("Wisconsin (WI)", 485,140),
	MI("Michigan (MI)", 540,120),
	NY("New York (NY)", 655,125),
	VT("Vermont (VT)", 655,60),
	NH("New Hampshire (NH)", 690,50),
	ME("Maine (ME)", 720,45),
	MA("Massachusetts (MA)", 740,95),
	RI("Rhode Island (RI)", 755,140),
	CT("Connecticut (CT)", 740,160),
	NJ("New Jersey (NJ)", 730,180),
	DE("Delaware (DE)", 720,200),
	MD("Maryland (MD)", 710,220);

	
	private String name; 
	private int x;
	private int y;
	
	EState(String name, int x, int y) {
		this.name= name;
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String getStateName() {
		return name;
	}
}