import java.awt.Image;

public class Hero extends THINGS{
	public String name="Hero";
	public int hp=10000;
	public int atk=1000;
	public int def=1000;
	public int money=0;
	public int ykey=90;
	public int bkey=90;
	public int rkey=90;
	public int floor=1;
	public int maxfloor=1;
	public int minfloor=1;
	public Image ph;
	public int x=7,y=13;
	public  Hero() {
		
	}
	public  Hero(String name,int hp,int atk,int def,int money,int ID) {
		this.name=name;
		this.hp=hp;
		this.atk=atk;
		this.def=def;
		this.money=money;
		this.ID=ID;
	}
	public  Hero(int hp,int atk,int def,int money,int floor) {
		this.hp=hp;
		this.atk=atk;
		this.def=def;
		this.money=money;
		this.floor=floor;
	}

}
