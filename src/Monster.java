import java.awt.Image;


public class Monster extends THINGS{
	public String name;
	public int hp;
	public int atk;
	public int def;
	public int meney;
	public int exp;
	public  Image ph;
	public  Monster(String name,int hp,int atk,int def,int meney,int exp,int ID) {
		this.name=name;
		this.hp=hp;
		this.atk=atk;
		this.def=def;
		this.meney=meney;
		this.exp=exp;
		this.ID=ID;
	}
}
