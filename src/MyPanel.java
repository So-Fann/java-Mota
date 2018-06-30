import java.io.*;
import java.net.Socket;
import java.awt.*;
import java.util.Arrays;
import java.awt.event.*;
import javax.swing.*;
import javazoom.jl.player.Player;

public class MyPanel extends JPanel implements KeyListener,MouseListener{
	private static final int WID= 480;//窗体的宽与高
    private static final int HEI= 480;
    private static final int ROW = 15;
    private static final int COL = 15;
    private static final int LEFT=1;
    private static final int RIGHT=2;
    private static final int UP=3;
    private static final int DOWN=0;
	private static final String SERVER_IP = "10.132.36.96"; // 服务端IP
	private static final int SERVER_PORT = 8080; // 服务端端口
	private ClientSocket cs = null;
	private FileOutputStream fos;

	private String ip = "10.132.36.96";// 设置成服务器IP

	private int port = 8821;

	private Socket client;

	private FileInputStream fis;

	private DataOutputStream dos;


	private int count;
    
    private int floor=1;//楼层
    private Hero role=new Hero();//创建主角
    private int x=7,y=13;//起始坐标位置
    private int maxfloor=1;//最高楼层
    public int[] sec=new int[50];
    private int spend=25;//商店初始价格
    private boolean disp=false;//npc是否消失
    private static final int CS = 32;
    private String[][] string= {
    		{  "",
    			"你就是国王派来的勇者吗?我等你很久了,我的力量被魔王封印了,但是我的智慧还在,我会在后面帮你的!\n噢,对了,3楼,7楼有宝物.你最好先拿到它们.",
    			"",
    			"",
    			"听说塔内有各式各样的道具,有的只能用一次,而有的可以永久使用."
    			},//1层
    		{	"",
    			"",
    			"",
    			"",
    			""},//2层
    		{	"",
        		"",
        		"",
        		"",
        		"怪物宝鉴合理使用会保障你的血量.塔里好像有个飞行器,可以在去过的楼层自由飞行."},//3层
    		{	"",
            	"",
            	"",
            	"这一层有一个锄头,可以挖掉周围的墙壁,但是,只能存在一个,而且,用过之后就会损毁,珍惜使用.",
            	""},//4层
    		{	"",
            	"",
                "",
                "这世上最强大的宝剑和盾牌就在里面.",
                "这一层还有一个隔间,需要从其他地方进入."},//5层
    		{	"",
                "",
                "",
                "",
                "机关门没有钥匙,但是,你把守卫打倒后就会自动小时."},//6层
    		{	"",
                "",
                "",
                "",
                ""},//7层
    		{	"",
                "",
                "",
                "",
                ""},//8层
    		{	"",
                "",
                "",
                "",
                ""},//9层
    		{	"",
                "",
                "",
                "",
                ""},//10层
    		{	"",
                "",
                "感谢你 勇士 把我从魔王手中救了出来",
                "",
                ""},//11层

    };//存储对话
    private int[][][] map= {
    		{//此层数无用
        		{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,2,3,4,5,6,7,8,1,1,0,0,0,501,1},
                {1,14,15,16,17,18,19,20,2,1,4,3,0,0,1},
                {1,0,0,0,1,0,0,0,0,0,1,0,0,0,1},
                {1,0,0,0,1,0,0,0,0,0,1,0,0,0,1},
                {1,53,54,55,0,1,1,101,1,1,105,106,1,0,1},
                {1,102,103,104,107,108,109,110,201,202,203,204,120,120,1},
                {1,400,401,402,403,404,405,406,407,408,409,410,120,120,1},
                {1,0,1,0,0,1,0,0,0,1,0,1,1,1,1},
                {1,102,102,0,0,1,0,0,0,1,0,1,0,0,1},
                {1,102,102,102,102,102,102,102,0,0,0,0,0,0,1},
                {1,0,1,0,0,0,0,0,0,0,1,0,1,0,1},
                {1,411,412,0,414,415,416,417,418,419,420,0,422,423,1},
                {1,424,425,426,0,0,0,0,0,1,0,0,1,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    		},{//第1层
    			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    			{1,4,4,4,4,4,1,501,1,4,4,4,4,4,1},
    			{1,4,4,4,4,4,1,800,1,4,4,4,4,4,1},
    			{1,5,5,5,5,1,0,0,0,1,5,5,5,5,1},
    			{1,5,5,5,5,1,0,0,0,1,5,5,5,5,1},
    			{1,5,5,5,5,1,0,0,0,1,5,5,5,5,1},
    			{1,1,1,1,1,1,0,0,0,1,1,1,1,1,1},
    			{1,5,5,5,1,804,0,0,0,0,1,5,5,5,1},
    			{1,5,5,5,1,102,102,102,102,102,1,5,5,5,1},
    			{1,5,5,5,1,1,0,0,0,1,1,5,5,5,1},
    			{1,5,5,5,5,1,0,0,0,1,5,5,5,5,1},
    			{1,5,5,5,5,1,0,0,0,1,5,5,5,5,1},
    			{1,5,5,5,5,1,0,0,0,1,5,5,5,5,1},
    			{1,5,5,5,5,1,0,0,0,1,5,5,5,5,1},
    			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            },
    		{//第2层
            	{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    			{1,106,106,102,102,102,1,500,1,1,15,0,102,16,1},
    			{1,106,106,102,102,102,1,0,0,1,0,1,1,0,1},
    			{1,105,105,107,107,107,1,1,0,1,15,201,0,19,1},
    			{1,105,105,0,0,0,0,1,0,1,0,1,201,1,1},
    			{1,1,32,1,1,1,1,1,18,0,105,1,18,103,1},
    			{1,0,0,0,0,0,0,1,0,1,1,1,105,102,1},
    			{1,1,36,0,36,1,0,203,16,15,106,1,106,102,1},
    			{1,1,1,204,1,1,1,1,0,1,105,1,1,1,1},
    			{1,108,0,0,0,0,0,1,0,1,0,16,0,18,1},
    			{1,414,0,0,0,0,422,1,0,1,1,1,1,0,1},
    			{1,103,105,107,108,106,104,1,0,201,18,103,1,0,1},
    			{1,103,105,107,108,106,104,1,18,1,102,102,1,0,1},
    			{1,103,105,107,108,106,104,1,102,1,108,108,1,501,1},
    			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            },{//第3层
            	{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    			{1,501,15,107,1,106,102,1,107,107,107,102,1,400,1},
    			{1,1,1,102,1,19,105,1,0,21,0,0,1,105,1},
    			{1,1,804,102,1,201,1,1,1,202,1,1,1,106,1},
    			{1,1,1,105,18,0,0,0,0,106,15,0,1,15,1},
    			{1,107,1,1,1,1,1,1,1,1,1,0,1,0,1},
    			{1,103,103,1,0,0,0,0,0,0,19,0,16,0,1},
    			{1,102,102,1,102,1,105,1,106,1,1,201,1,0,1},
    			{1,102,19,1,201,1,1,1,1,1,103,0,1,0,1},
    			{1,19,19,202,201,1,107,15,201,102,15,0,1,0,1},
    			{1,0,0,1,1,1,1,201,1,1,1,1,1,0,1},
    			{1,1,1,1,201,201,0,105,18,18,0,19,106,0,1},
    			{1,18,107,0,0,1,1,1,1,201,1,201,1,0,1},
    			{1,411,18,108,0,1,106,105,102,18,1,107,1,500,1},
    			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            },{//第4层
            	{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    			{1,500,0,15,16,0,0,18,108,0,0,0,18,102,1},
    			{1,1,1,1,1,1,1,0,1,1,107,1,106,1,1},
    			{1,102,1,102,1,101,1,0,1,15,202,1,1,1,1},
    			{1,105,1,105,1,0,0,0,1,201,1,0,0,0,1},
    			{1,21,1,19,1,18,1,0,1,18,108,201,1,0,1},
    			{1,0,0,0,201,201,1,0,1,19,1,1,1,18,1},
    			{1,0,1,1,1,1,1,501,1,1,102,1,19,0,1},
    			{1,0,0,1,103,106,1,1,1,105,105,105,1,0,1},
    			{1,1,0,1,105,18,19,21,21,106,106,106,1,201,1},
    			{1,803,201,1,1,1,21,1,18,1,1,1,0,201,1},
    			{1,1,201,19,21,14,0,1,107,107,0,203,0,1,1},
    			{1,102,107,1,1,1,18,1,107,107,0,1,203,1,1},
    			{1,105,106,1,105,106,0,1,107,107,0,1,203,402,1},
    			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            },{//第5层
            	{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    			{1,501,0,14,0,201,108,1,102,102,102,120,103,103,1},
    			{1,1,1,1,201,1,1,1,102,102,102,418,106,106,1},
    			{1,102,102,1,0,202,102,1,108,105,105,426,105,105,1},
    			{1,105,21,201,0,1,108,1,1,1,1,204,1,1,1},
    			{1,1,1,1,202,1,1,1,1,1,29,0,29,1,1},
    			{1,804,0,21,0,0,14,0,1,0,0,0,0,1,1},
    			{1,103,105,1,0,0,1,500,1,0,0,0,0,0,1},
    			{1,20,20,1,0,0,1,1,1,1,1,1,202,1,1},
    			{1,102,0,0,0,0,0,0,1,0,0,0,201,21,1},
    			{1,201,1,1,201,1,1,201,1,25,1,1,1,1,1},
    			{1,0,1,1,21,102,1,0,1,0,0,107,23,803,1},
    			{1,21,102,1,1,1,1,0,1,0,0,0,107,23,1},
    			{1,0,21,18,20,0,102,0,1,106,0,0,0,107,1},
    			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            },{//第6层
            	{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    			{1,500,0,1,102,21,105,106,107,1,105,106,107,108,1},
    			{1,1,201,1,1,202,1,1,1,1,105,106,107,108,1},
    			{1,0,18,201,201,19,1,108,1,0,0,0,0,23,1},
    			{1,202,1,1,1,201,1,201,1,1,1,1,1,202,1},
    			{1,0,0,0,1,0,202,0,0,0,201,201,19,107,1},
    			{1,1,0,0,1,18,1,0,0,0,1,1,0,1,1},
    			{1,107,19,0,1,0,1,0,0,0,1,0,0,0,1},
    			{1,19,0,0,1,0,1,0,0,0,1,202,1,1,1},
    			{1,1,1,0,201,20,1,0,0,0,0,19,1,107,1},
    			{1,804,1,1,1,201,1,1,1,1,0,0,1,107,1},
    			{1,21,0,0,0,0,0,0,0,1,1,1,1,105,1},
    			{1,1,201,1,1,1,1,1,0,0,0,0,18,21,1},
    			{1,106,102,1,501,0,108,201,0,0,0,0,0,18,1},
    			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            },{//第7层
            	{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    			{1,501,1,102,1,19,0,102,0,19,1,102,1,15,1},
    			{1,106,1,21,1,107,1,202,1,107,1,21,1,15,1},
    			{1,0,1,0,1,0,1,108,1,0,1,0,1,105,1},
    			{1,0,1,107,1,20,1,19,1,20,1,107,1,0,1},
    			{1,21,1,201,1,201,1,105,1,201,1,201,1,21,1},
    			{1,0,0,0,0,0,1,419,1,0,0,0,0,0,1},
    			{1,201,1,4,4,4,4,4,4,4,4,4,1,201,1},
    			{1,0,1,4,4,4,4,4,4,4,4,4,1,0,1},
    			{1,0,0,0,19,0,107,0,107,0,19,0,0,0,1},
    			{1,22,1,0,1,1,1,1,1,1,1,1,1,23,1},
    			{1,16,1,19,0,102,19,0,0,107,0,0,1,16,1},
    			{1,105,1,1,1,1,1,1,1,1,1,0,1,106,1},
    			{1,102,1,1,500,0,15,102,16,102,17,102,1,103,1},
    			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            },{//第8层
            	{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    			{1,500,1,803,1,1,23,23,23,1,1,804,1,501,1},
    			{1,0,1,22,0,1,1,0,1,1,0,22,1,0,1},
    			{1,21,201,21,0,0,0,0,0,0,0,23,201,21,1},
    			{1,1,1,1,202,1,1,1,1,1,1,1,1,1,1},
    			{1,0,201,201,21,0,0,0,202,21,201,0,0,0,1},
    			{1,0,1,1,1,1,1,1,1,1,1,1,1,102,1},
    			{1,24,1,4,4,4,4,4,4,4,4,4,1,0,1},
    			{1,24,1,4,4,106,106,401,105,105,4,4,1,202,1},
    			{1,24,1,4,4,102,102,104,103,103,4,4,1,0,1},
    			{1,0,1,4,4,4,4,204,4,4,4,4,1,0,1},
    			{1,201,1,4,4,1,29,22,29,1,4,4,1,108,1},
    			{1,201,1,1,1,1,1,202,1,1,1,1,1,0,1},
    			{1,0,24,201,201,201,0,21,0,0,0,105,106,0,1},
    			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            },{//第9层
            	{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    			{1,102,108,0,1,0,105,106,0,1,0,107,1,500,1},
    			{1,420,107,0,1,0,102,103,0,1,0,0,1,0,1},
    			{1,102,108,0,202,27,0,0,0,1,0,0,1,0,1},
    			{1,0,0,0,1,0,0,0,0,1,26,0,201,25,1},
    			{1,1,1,1,1,1,1,1,201,1,201,1,1,1,1},
    			{1,804,23,0,0,0,0,0,24,1,0,0,0,803,1},
    			{1,23,0,0,0,0,0,1,1,1,1,1,202,1,1},
    			{1,1,1,1,0,0,0,1,0,0,24,0,24,107,1},
    			{1,0,0,1,0,0,0,1,0,0,1,0,0,0,1},
    			{1,501,25,201,0,0,25,201,0,0,1,1,1,1,1},
    			{1,0,0,1,0,0,0,1,0,0,1,0,0,805,1},
    			{1,1,1,1,0,0,0,1,1,1,1,0,0,0,1},
    			{1,105,107,201,0,0,22,201,106,0,201,0,0,0,1},
    			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            },{//第10层
            	{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    			{1,5,5,1,1,1,1,501,1,1,1,1,5,5,1},
    			{1,5,5,1,1,1,1,28,1,1,1,1,5,5,1},
    			{1,5,5,1,0,0,0,0,0,0,0,1,5,5,1},
    			{1,5,5,1,21,21,21,0,21,21,21,1,5,5,1},
    			{1,5,5,1,21,21,21,0,21,21,21,1,5,5,1},
    			{1,5,5,1,21,21,21,0,21,21,21,1,5,5,1},
    			{1,5,5,1,21,21,21,0,21,21,21,1,5,5,1},
    			{1,5,5,1,1,1,0,0,0,1,1,1,5,5,1},
    			{1,5,5,1,1,1,0,0,0,1,1,1,5,5,1},
    			{1,500,0,0,0,0,0,0,0,0,0,0,0,0,1},
    			{1,5,5,5,5,5,5,5,5,5,5,5,5,202,1},
    			{1,5,5,5,5,5,5,5,5,5,5,5,5,203,1},
    			{1,501,0,0,0,0,0,0,0,0,0,0,0,1,1},
    			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            },{//第11层
            	{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    			{1,0,0,0,0,0,1,500,1,0,0,0,0,0,1},
    			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
    			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
    			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
    			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
    			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
    			{1,0,0,0,0,0,0,802,0,0,0,0,0,0,1},
    			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
    			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
    			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
    			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
    			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
    			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
    			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            },{//第12层
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,5,5,5,5,5,5,5,5,5,5,5,5,5,1},
			{1,419,0,421,110,110,422,423,110,110,424,425,110,5,1},
			{1,0,5,5,5,5,5,5,5,5,5,5,110,5,1},
			{1,0,5,426,110,109,108,107,110,109,108,5,110,5,1},
			{1,0,5,425,5,5,5,5,5,5,107,5,110,5,1},
			{1,0,5,424,5,109,108,107,110,5,110,5,110,5,1},
			{1,0,5,423,5,110,5,5,110,5,109,5,110,5,1},
			{1,0,5,422,5,107,5,500,110,5,108,5,110,5,1},
			{1,0,5,421,5,108,5,5,5,5,107,5,110,5,1},
			{1,0,5,420,5,109,110,107,108,109,110,5,110,5,1},
			{1,0,5,419,5,5,5,5,5,5,5,5,110,5,1},
			{1,0,5,105,106,105,106,105,106,105,106,105,110,5,1},
			{1,0,0,5,5,5,5,5,5,5,5,5,5,5,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
	},
    	};
    public String[] shoplist= {"血量加"+((this.floor)/10+1)*200,"  攻加"+((this.floor)/10+1)*2,
			"  防加"+((this.floor)/10+1)*3,"我买不起!"};//商店选项列表
    private Image floor1Image;
    private Image wall1Image;
    private Image floor2Image;
    private Image wall2Image;
    private Image floor3Image;
    private Image wall3Image;
    private Image wall4Image;
    private Image wall5Image;
    private Image wall6Image;
    private Image wall7Image;
    private Image wall8Image;

    public ImageIcon[] goodph=new ImageIcon[60];
    private Monster[] m=new Monster[60];//创建怪物
    private shop shops=new shop("商店",101);//创建商店
    private key[] keys=new key[3];//创建钥匙
    private drug[] drugs=new drug[4];//创建药品
    private jewel[] jewels=new jewel[2];//属性宝石
    private door[] doors=new door[4];//创建门
    private Goods[] goods=new Goods[12];//创建物品
    private JLabel[] wp=new JLabel[12];
    private sword[] swords=new sword[8];//创建剑
    private shield[] shields=new shield[8];//创建盾
    private stair up=new stair("up",501);//创建上楼梯
    private stair down=new stair("down",500);//创建下楼梯
    private NPC[] npc=new NPC[6];
    
    InputStream soundofgoods=this.getClass().getResourceAsStream("/res/Audio/SE/get.mp3");
    InputStream soundofdoors=this.getClass().getResourceAsStream("/res/Audio/SE/door.mp3");
    InputStream soundoffight=this.getClass().getResourceAsStream("/res/Audio/SE/fight.mp3");
    InputStream soundoffightover=this.getClass().getResourceAsStream("/res/Audio/SE/fightover.mp3");
    Player soundofkey;

    JFrame mainframe=new JFrame("MagicTower");
    JDialog fig;
    boolean Fighting=false;
    boolean check=false;
    Monster FightingMonster=null;
    JPanel showfighting;
    JLabel roleph,mph,roleatk,roledef,rolehp,matk,mdef,mhp,fightph;
    JDialog shop;
    JDialog dialogc;
    Container contentPane;
    JLabel showMes=new JLabel("!!!!!");
    JLabel showfloor=new JLabel("当前第"+floor+"层");
    
    //存读档面板
    JDialog fileD;
    JPanel filep;
    boolean isSave=true;
    JLabel filetip,file1,file2,file3,filetip2;
    
    //设定显示图像对象
    JLabel showph;
    JLabel showhp,hp;
	JLabel showatk,atk;
	JLabel showdef,def;
	JLabel showmon,mon;
	JLabel showykey,ykey;
	JLabel showbkey,bkey;
	JLabel showrkey,rkey;
	JLabel load,save;
	JList sh;
	
	//声音线程
    public Thread threadofgoods,threadofdoors,threadoffight,threadoffightover;
    
    public MyPanel() {
    	this.setPreferredSize(new Dimension(this.WID+225, this.HEI));//设定初始构造时面板大小
    	//初始化游戏数据
    	this.floor=this.role.floor;
    	this.setLayout(null);
    	this.CreateMonster();//创建怪物
    	this.CreateGoods();//创建物品
    	this.loadImage();//于初始化时载入图形
    	this.creatSound();//创建声音
    	this.CreateMenuBar();//创建游戏菜单栏
    	this.showAttribute();//属性展示界面
        this.setFocusable(true);//设定焦点在本窗体
        this.addKeyListener(this);//
        count=UP;
        sec[0]=0;
        FightingMonster=m[14];
        //主窗体
        mainframe.setDefaultCloseOperation(mainframe.EXIT_ON_CLOSE);
        mainframe.setSize(800, 600);
        mainframe.setLocation(200,100);
		contentPane=mainframe.getContentPane();
		contentPane.setSize(800,600);
		contentPane.add(this,BorderLayout.CENTER);
		mainframe.pack();
		showMes.setText("游戏初始化完毕!");
		this.setFocusable(true);
		mainframe.setResizable(false);
		mainframe.setVisible(true);
    }
    
    private void showAttribute() {//展示属性
		//各属性的显示
		showph = new JLabel();
		showph.setIcon(new ImageIcon(getClass().getResource("/res/image/Items/role.png")));
		showph.setBounds(480+10, 40, 36, 38);
		showph.setBorder(BorderFactory.createLineBorder(Color.gray));
		
		showhp = new JLabel();
		showhp.setIcon(new ImageIcon(getClass().getResource("/res/image/Items/hp.png")));
		showhp.setBounds(480+60, 45, 32, 32);

		hp=new JLabel("" + role.hp);
		hp.setBounds(480+120, 45, 120, 32);
		hp.setForeground(Color.white);
		hp.setFont(new Font("宋体",Font.BOLD, 20));
		
		showatk = new JLabel();
		showatk.setIcon(new ImageIcon(getClass().getResource("/res/image/Items/atk.png")));
		showatk.setBounds(480+60, 45+32*1, 32, 32);
		
		atk=new JLabel("" + role.atk);
		atk.setBounds(480+120, 45+32*1, 120, 32);
		atk.setForeground(Color.white);
		atk.setFont(new Font("宋体",Font.BOLD, 20));
		
		showdef = new JLabel();
		showdef.setIcon(new ImageIcon(getClass().getResource("/res/image/Items/def.png")));
		showdef.setBounds(482+60, 45+32*2, 32, 32);
		
		def=new JLabel("" + role.def);
		def.setBounds(480+120, 45+32*2, 120, 32);
		def.setForeground(Color.white);
		def.setFont(new Font("宋体",Font.BOLD, 20));

		showmon = new JLabel();
		showmon.setIcon(new ImageIcon(getClass().getResource("/res/image/Items/money.png")));
		showmon.setBounds(480+60, 45+32*3, 32, 32);
		
		mon=new JLabel("" + role.money);
		mon.setBounds(480+120, 45+32*3, 120, 32);
		mon.setForeground(Color.white);
		mon.setFont(new Font("宋体",Font.BOLD, 20));
		
		showykey = new JLabel();
		showykey.setIcon(new ImageIcon(getClass().getResource("/res/image/Items/102.png")));
		showykey.setBounds(483+66*0, 45+32*4, 32, 32);
		
		ykey=new JLabel("" + role.ykey);
		ykey.setBounds(483+32+66*0, 48+32*4, 32, 32);
		ykey.setForeground(Color.white);
		ykey.setFont(new Font("宋体",Font.BOLD, 22));

		showbkey = new JLabel();
		showbkey.setIcon(new ImageIcon(getClass().getResource("/res/image/Items/103.png")));
		showbkey.setBounds(483+66*1, 45+32*4, 32, 32);
		
		bkey=new JLabel("" + role.bkey);
		bkey.setBounds(483+32+66*1, 48+32*4, 32, 32);
		bkey.setForeground(Color.white);
		bkey.setFont(new Font("宋体",Font.BOLD, 22));
		
		showrkey = new JLabel();
		showrkey.setIcon(new ImageIcon(getClass().getResource("/res/image/Items/104.png")));
		showrkey.setBounds(483+66*2, 45+32*4, 32, 32);
		
		rkey=new JLabel("" + role.rkey);
		rkey.setBounds(483+32+66*2, 48+32*4, 32, 32);
		rkey.setForeground(Color.white);
		rkey.setFont(new Font("宋体",Font.BOLD, 22));
		
		save=new JLabel("存档",SwingConstants.CENTER);
		save.setForeground(Color.white);
		save.setFont(new Font("宋体",Font.BOLD, 22));
		save.setBounds(480+20, 55+32*5, 60, 35);
		save.setOpaque(false);
		save.setBorder(BorderFactory.createLineBorder(Color.white));
		save.addMouseListener(this);
		
		load=new JLabel("读档",SwingConstants.CENTER);
		load.setForeground(Color.white);
		load.setFont(new Font("宋体",Font.BOLD, 22));
		load.setBounds(480+20+90, 55+32*5, 60, 35);
		load.setOpaque(false);
		load.setBorder(BorderFactory.createLineBorder(Color.white));
		load.addMouseListener(this);
		
		showfloor.setForeground(Color.white);
		showfloor.setFont(new Font("宋体",Font.BOLD, 18));
		showfloor.setBounds(480+50,260,200, 35);
		
		showMes.setForeground(Color.white);
		showMes.setFont(new Font(null,1, 22));
		showMes.setBounds(125, 0,350, 35);

		int index=0;
		for(int n=0;n<1;n++) {
			for(int m=0;m<4;m++) {
				wp[index]=new JLabel();
				wp[index].setHorizontalTextPosition(SwingConstants.CENTER);
				wp[index].setText("X");
				wp[index].setFont(new Font("默认",1, 30));
				wp[index].setForeground(Color.red);
				wp[index].setBounds(480+5+47*m, 314, 40, 40);
				if(index<3) {
					wp[index].setIcon(new ImageIcon(getClass().getResource("/res/image/Items/"+(index+400)+".png")));
				}
				
				wp[index].addMouseListener(this);
				this.add(wp[index]);
				index++;
			}
		}
    
		
		this.add(showph);
		this.add(showhp);
		this.add(hp);
		this.add(showatk);
		this.add(atk);
		this.add(showdef);
		this.add(def);
		this.add(showmon);
		this.add(mon);
		this.add(showykey);
		this.add(ykey);
		this.add(showbkey);
		this.add(bkey);
		this.add(showrkey);
		this.add(rkey);
		this.add(save);
		this.add(load);
		this.add(showfloor);
		this.add(showMes);
	}
    
    private void CreateMenuBar() {
		JMenuBar bar=new JMenuBar();
		bar.setBackground(Color.WHITE);
		mainframe.setJMenuBar(bar);
		JMenu menu1=new JMenu("游戏");
		JMenuItem item1=new JMenuItem("新游戏");
		JMenuItem item2=new JMenuItem("读取进度");
		JMenuItem item3=new JMenuItem("存储进度");
		JMenuItem item4=new JMenuItem("退出游戏");
		item1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new MyPanel();
				mainframe.dispose();
			}
		});
		item2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isSave=false;
				showFileofGame(isSave);
			}
		});
		item3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isSave=true;
				showFileofGame(isSave);
			}
		});
		item4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainframe.dispose();
			}
		});
		menu1.add(item1);
		menu1.addSeparator();
		menu1.add(item2);
		menu1.add(item3);
		menu1.addSeparator();
		menu1.add(item4);
		JMenu menu2=new JMenu("规则与说明");
		JMenuItem item5=new JMenuItem("游戏说明");
		item5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JDialog help=new JDialog(mainframe, "游戏说明", true);
				help.setSize(480,360);
				help.setLocation(mainframe.getLocation().x+80,mainframe.getLocation().y+100);
				JLabel helpph=new JLabel();
				helpph.setSize(450,340);
				ImageIcon imageIcon=new ImageIcon(getClass().getResource("/res/image/Skins/help.png"));
				Image temp = imageIcon.getImage().getScaledInstance(helpph.getWidth(),
						helpph.getHeight(), imageIcon.getImage().SCALE_DEFAULT);  
				imageIcon=new ImageIcon(temp);
				helpph.setIcon(imageIcon);
				help.add(helpph);
				help.pack();
				help.setVisible(true);
			}
		});
		JMenuItem item7=new JMenuItem("游戏规则");
		menu2.add(item7);
		item7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JDialog about=new JDialog(mainframe, "游戏规则", true);
				about.setLocation(mainframe.getLocation().x+50, mainframe.getLocation().y+90);
				about.setSize(500,300);
				JTextArea shows=new JTextArea();
				shows.setEditable(false);
				shows.setFont(new Font("默认",1, 20));
				JScrollPane show=new JScrollPane(shows);
				shows.append("   规则:\n      上下左右控制英雄行走,碰到怪物触发战斗,\n"+ 
						"      注意生命值,胜利会获得金币\n" + 
						"      获得宝石会增加攻击防御力\n"+ 
						"      获得血瓶会增加生命值\n"+ 
						"      还有各类道具,详情查看游戏说明\n" + 
						"      还有武器和盾牌会增加大量攻击防御\n" + 
						"      可以在商店用金币购买属性");
				about.add(show);
				about.setVisible(true);
			}
		});
		
		menu2.add(item5);
		
		
		bar.add(menu1);
		bar.add(menu2);
		
	}

	private void showShop() {
		// TODO Auto-generated method stub
    	shop=new JDialog(mainframe,null,true);
    	shop.setSize(140, 130);
    	shop.setUndecorated(true);
    	JPanel panel=new JPanel(null);
    	JLabel shopph=new JLabel();
    	JLabel tip=new JLabel("需"+spend+"个金币购买:");
    	tip.setBounds(10, 25, 120, 40);
    	tip.setFont(new Font("宋体",Font.BOLD, 12));
    	tip.setBackground(Color.BLACK);
    	tip.setForeground(Color.green);
    	shopph.setSize(97,33);
    	shopph.setIcon(new ImageIcon(getClass().getResource("/res/image/Items/10000.png")));
    	shopph.setBounds(24,3, 100, 32);
		sh=new JList(shoplist);
		sh.setSelectedIndex(0);
		sh.setFont(new Font("宋体",Font.BOLD, 14));
		sh.setBackground(Color.BLACK);
		sh.setForeground(Color.WHITE);
		sh.setBounds(23, 58, 90, 160);
		sh.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {
			}
			public void mousePressed(MouseEvent arg0) {
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mouseEntered(MouseEvent arg0) {
			}
			public void mouseClicked(MouseEvent arg0) {//鼠标双击
				if(arg0.getClickCount()==2) {
					JList sourse=(JList)arg0.getSource();
					Object[] selection=sourse.getSelectedValues();
					ShopDoAction(selection);
				}
			}
		});
		sh.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {	
			}
			public void keyReleased(KeyEvent arg0) {
			}
			public void keyPressed(KeyEvent arg0) {//回车键
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER) {
					JList sourse=(JList)arg0.getSource();
					Object[] selection=sourse.getSelectedValues();
					ShopDoAction(selection);
				}
			}
		});
		panel.setBackground(Color.BLACK);
		panel.add(shopph);
		panel.add(tip);
		panel.add(sh);
		shop.add(panel);
		shop.setLocation(mainframe.getLocation().x+190,mainframe.getLocation().y+140);
		sh.setFocusable(true);
		shop.setVisible(true);
	}
    private void ShopDoAction(Object[] selection) {
		if (role.money >= spend) {
			if (((String) selection[0]).equals(shoplist[0].toString())) {
				role.hp += ((floor) / 10 + 1) * 200;
				role.money -= spend;
				spend +=spend/2;
				mon.setText("" + role.money);
				showMes.setText("你购买了" + ((floor) / 10 + 1) * 200 + "血量!");
				hp.setText("" + role.hp);
				shop.setVisible(false);
			} else if (((String) selection[0]).equals(shoplist[1].toString())) {
				role.atk += ((floor) / 10 + 1) * 2;
				role.money -= spend;
				spend +=spend/2;
				mon.setText("" + role.money);
				showMes.setText("你购买了" + ((floor) / 10 + 1) * 2 + "攻击!");
				atk.setText("" + role.atk);
				shop.setVisible(false);
			} else if (((String) selection[0]).equals(shoplist[2].toString())) {
				role.def += ((floor) / 10 + 1) * 3;
				role.money -= spend;
				spend += spend/2;
				mon.setText("" + role.money);
				showMes.setText("你购买了" + ((floor) / 10 + 1) * 3 + "防御!");
				def.setText("" + role.def);
				shop.setVisible(false);
			} else {
				showMes.setText("你啥都没买!");
				shop.setVisible(false);
			}
		}else {
			showMes.setText("买不起,别装逼!");
			shop.setVisible(false);
		}
	}
    
    private void showFileofGame(boolean isSave) {
    	fileD=new JDialog(mainframe, null, true);
    	fileD.setSize(300, 315);
    	fileD.setUndecorated(true);
    	fileD.setLocation(mainframe.getLocation().x+95, mainframe.getLocation().y+100);
    	filep=new JPanel(null);
    	filep.setBackground(Color.black);
    	
    	if(isSave) {
    		filetip=new JLabel("请选择位置存档:");
    	}else {
    		filetip=new JLabel("请选择一个读档:");
    	}
    	filetip.setBounds(20, 5, 200, 30);
    	filetip.setFont(new Font("宋体",Font.BOLD, 18));
    	filetip.setForeground(Color.white);
    	
    	file1=new JLabel("存档1",SwingConstants.CENTER);
    	file1.setBounds(46,50, 200, 70);
    	file1.setFont(new Font("宋体",Font.BOLD, 32));
    	file1.setForeground(Color.white);
    	file1.setBorder(BorderFactory.createLineBorder(Color.white));
    	file1.addMouseListener(this);
    	
    	file2=new JLabel("存档2",SwingConstants.CENTER);
    	file2.setBounds(46,125, 200, 70);
    	file2.setFont(new Font("宋体",Font.BOLD, 32));
    	file2.setForeground(Color.white);
    	file2.setBorder(BorderFactory.createLineBorder(Color.white));
    	file2.addMouseListener(this);
    	
    	file3=new JLabel("存档3",SwingConstants.CENTER);
    	file3.setBounds(46,205, 200, 70);
    	file3.setFont(new Font("宋体",Font.BOLD, 32));
    	file3.setForeground(Color.white);
    	file3.setBorder(BorderFactory.createLineBorder(Color.white));
    	file3.addMouseListener(this);
    	
    	filetip2=new JLabel("按空格键退出此界面",SwingConstants.CENTER);
    	filetip2.setBounds(43, 290, 200, 20);
    	filetip2.setFont(new Font("宋体",Font.BOLD, 18));
    	filetip2.setForeground(Color.white);
    	
    	filep.add(filetip);
    	filep.add(file1);
    	filep.add(file2);
    	filep.add(file3);
    	filep.add(filetip2);
    	filep.addMouseListener(this);
    	
    	fileD.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_SPACE) {
					fileD.setVisible(false);
				}
			}
			public void keyPressed(KeyEvent arg0) {}
		});
    	fileD.add(filep);
    	fileD.setVisible(true);
    }
	public void sendFile(Socket client) throws Exception {
		try {
			File file = new File("E:\\MTower\\存档3.dat");
			if(file.exists()) {
				fis = new FileInputStream(file);
				dos = new DataOutputStream(client.getOutputStream());
				// 文件名和长度
				dos.writeUTF(file.getName());
				dos.flush();
				dos.writeLong(file.length());
				dos.flush();

				// 开始传输文件
				System.out.println("======== 开始传输文件 ========");
				byte[] bytes = new byte[1024];
				int length = 0;
				long progress = 0;
				while((length = fis.read(bytes, 0, bytes.length)) != -1) {
					dos.write(bytes, 0, length);
					dos.flush();
					progress += length;
					System.out.print("| " + (100*progress/file.length()) + "% |");
				}
				System.out.println();
				System.out.println("======== 文件传输成功 ========");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fis != null)
				fis.close();
			if(dos != null)
				dos.close();
			client.close();
		}
	}
	public void DownFile() throws IOException{
//从服务器端下载文件
		try {
			//while(true){
				Socket socket = new Socket(ip,port);
				DataInputStream is = new  DataInputStream(socket.getInputStream());
				OutputStream os = socket.getOutputStream();
				//1、得到文件名
				String filename="E:\\MTower\\";
				filename += is.readUTF();
				System.out.println("新生成的文件名为:"+filename);
				FileOutputStream fos = new FileOutputStream(filename);
				byte[] b = new byte[1024];
				int length = 0;
				while((length=is.read(b))!=-1){
					//2、把socket输入流写到文件输出流中去
					fos.write(b, 0, length);
				}
				fos.flush();
				fos.close();
				is.close();
				socket.close();
			//}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void WebGameFile(JLabel filen, boolean isSave) throws Exception {
		String rn="\r\n";
		String space="\0";
		if(isSave) {//存档
			try {
				FileOutputStream out=new FileOutputStream(filen.getText()+".dat");
				//角色信息
				String str=String.valueOf(role.hp+"\0"+role.atk+"\0"+role.def+"\0"+role.money+"\0"+
						role.ykey+"\0"+role.bkey+"\0"+role.rkey+"\0"+role.x+"\0"+role.y+
						"\0"+role.floor+"\0"+role.minfloor+"\0"+role.maxfloor+"\0"+spend);
				byte[] b=str.getBytes();
				for(int gs=0;gs<b.length;gs++) {
					out.write(b[gs]);
				}
				//回车
				out.write(rn.getBytes());
				//道具情况
				for(int p=0;p<3;p++) {
					str=String.valueOf(goods[p].ex+"\0");
					byte[] bg=str.getBytes();
					for(int gg=0;gg<bg.length;gg++) {
						out.write(bg[gg]);
					}
				}
				//回车
				out.write(rn.getBytes());
				//地图
				for(int i=0;i<map.length;i++) {
					for(int j=0;j<map[i].length;j++) {
						for(int k=0;k<map[i][j].length;k++) {
							str=String.valueOf(map[i][j][k]+"\0");
							byte[] bytes=str.getBytes();
							for(int g=0;g<bytes.length;g++) {
								out.write(bytes[g]);
							}
						}
					}
				}
				out.close();
				try{
					Socket client = new Socket(SERVER_IP, SERVER_PORT);
					sendFile(client);
					showMes.setText("存档成功了");
				}catch(Exception e){
					showMes.setText("存档失败");
					e.printStackTrace();
				}
				fileD.setVisible(false);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}



		}else {//读档
			DownFile();
			System.out.println("2");
			FileReader reader=new FileReader(filen.getText()+".dat");
			BufferedReader br=new BufferedReader(reader);
			String str;
			//读取属性
			str=br.readLine();
			String[] strings=str.split("\0");
			System.out.println(strings.length);
			for(int g=0;g<strings.length;g++) {
				System.out.println(strings[g]);
			}

			role.hp=Integer.parseInt(strings[0]);
			role.atk=Integer.parseInt(strings[1]);
			role.def=Integer.parseInt(strings[2]);
			role.money=Integer.parseInt(strings[3]);
			role.ykey=Integer.parseInt(strings[4]);
			role.bkey=Integer.parseInt(strings[5]);
			role.rkey=Integer.parseInt(strings[6]);
			x=Integer.parseInt(strings[7]);
			y=Integer.parseInt(strings[8]);
			role.floor=Integer.parseInt(strings[9]);
			maxfloor=role.maxfloor;
			floor=role.floor;
			role.minfloor=Integer.parseInt(strings[10]);
			role.maxfloor=Integer.parseInt(strings[11]);
			spend=Integer.parseInt(strings[12]);

			//读取道具
			str=br.readLine();
			strings=str.split("\0");
			for(int g=0;g<strings.length;g++) {
				goods[g].ex=Boolean.parseBoolean(strings[g]);
				System.out.println(goods[g].ex);
			}

			//读取地图
			str=br.readLine();
			strings=str.split("\0");
			int index=0;
			for(int i=0;i<map.length;i++) {
				for(int j=0;j<map[i].length;j++) {
					for(int k=0;k<map[i][j].length;k++) {
						map[i][j][k]=Integer.parseInt(strings[index]);
						System.out.println(strings[index]);
						index++;
					}
				}
			}
			showMes.setText("读档成功了");
			fileD.setVisible(false);
			repaint();
		}

	}
    private void GameFile(JLabel filen, boolean isSave) throws Exception {
    	String rn="\r\n";
		String space="\0";
    	if(isSave) {//存档
    		try {
				FileOutputStream out=new FileOutputStream(filen.getText()+".dat");
				//角色信息
				String str=String.valueOf(role.hp+"\0"+role.atk+"\0"+role.def+"\0"+role.money+"\0"+
									role.ykey+"\0"+role.bkey+"\0"+role.rkey+"\0"+role.x+"\0"+role.y+
									"\0"+role.floor+"\0"+role.minfloor+"\0"+role.maxfloor+"\0"+spend);
				byte[] b=str.getBytes();
				for(int gs=0;gs<b.length;gs++) {
						out.write(b[gs]);
				}
				//回车
				out.write(rn.getBytes());
				//道具情况
				for(int p=0;p<3;p++) {
					str=String.valueOf(goods[p].ex+"\0");
					byte[] bg=str.getBytes();
					for(int gg=0;gg<bg.length;gg++) {
							out.write(bg[gg]);
					}
				}
				//回车
				out.write(rn.getBytes());
				//地图
				for(int i=0;i<map.length;i++) {
					for(int j=0;j<map[i].length;j++) {
						for(int k=0;k<map[i][j].length;k++) {
							str=String.valueOf(map[i][j][k]+"\0");
							byte[] bytes=str.getBytes();
							for(int g=0;g<bytes.length;g++) {
									out.write(bytes[g]);
							}
						}
					}
				}
				out.close();
				showMes.setText("存档成功");
				fileD.setVisible(false);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
    		
    		
    	
    	}else {//读档
    		FileReader reader=new FileReader(filen.getText()+".dat");
    		BufferedReader br=new BufferedReader(reader);
    		String str;
    		//读取属性
    		str=br.readLine();
    		String[] strings=str.split("\0");
    		System.out.println(strings.length);
    		for(int g=0;g<strings.length;g++) {
    			System.out.println(strings[g]);
    		}
    		
    		role.hp=Integer.parseInt(strings[0]);
    		role.atk=Integer.parseInt(strings[1]);
    		role.def=Integer.parseInt(strings[2]);
    		role.money=Integer.parseInt(strings[3]);
    		role.ykey=Integer.parseInt(strings[4]);
    		role.bkey=Integer.parseInt(strings[5]);
    		role.rkey=Integer.parseInt(strings[6]);
    		x=Integer.parseInt(strings[7]);
    		y=Integer.parseInt(strings[8]);
    		role.floor=Integer.parseInt(strings[9]);
    		maxfloor=role.maxfloor;
    		floor=role.floor;
    		role.minfloor=Integer.parseInt(strings[10]);
    		role.maxfloor=Integer.parseInt(strings[11]);
    		spend=Integer.parseInt(strings[12]);
    		
    		//读取道具
    		str=br.readLine();
    		strings=str.split("\0");
    		for(int g=0;g<strings.length;g++) {
    			goods[g].ex=Boolean.parseBoolean(strings[g]);
    			System.out.println(goods[g].ex);
    		}
    	
    		//读取地图
    		str=br.readLine();
    		strings=str.split("\0");
    		int index=0;
    		for(int i=0;i<map.length;i++) {
				for(int j=0;j<map[i].length;j++) {
					for(int k=0;k<map[i][j].length;k++) {
						map[i][j][k]=Integer.parseInt(strings[index]);
						System.out.println(strings[index]);
						index++;
					}
				}
			}
    		showMes.setText("读档成功");
    		fileD.setVisible(false);
    		repaint();
    	}
    	
	}
    
	private void showFig() {
		JLabel tip=new JLabel("正在战斗");
		tip.setFont(new Font("宋体",Font.BOLD, 20));
		tip.setBounds(100,5, 100, 20);
		tip.setForeground(Color.RED);
		roleph=new JLabel();
		roleph.setSize(32,32);
		roleph.setIcon(new ImageIcon(getClass().getResource("/res/image/Items/role.png")));
		roleph.setBounds(10, 25, 32, 32);
		roleatk=new JLabel("攻:"+role.atk);
		roleatk.setBounds(45,28, 80, 20);
		roledef=new JLabel("防:"+role.def);
		roledef.setBounds(45,60, 80, 20);
		rolehp=new JLabel("血:"+role.hp);
		rolehp.setBounds(10,100, 100, 20);
		rolehp.setFont(new Font("宋体",Font.BOLD,18));
		rolehp.setForeground(Color.green);
		fightph=new JLabel();
		fightph.setSize(100,70);
		ImageIcon fightingph=new ImageIcon(getClass().getResource("/res/image/Characters/fighting.gif"));
		Image temp = fightingph.getImage().getScaledInstance(fightph.getWidth(),
				fightph.getHeight(), fightingph.getImage().SCALE_DEFAULT);  
		fightingph = new ImageIcon(temp);
		fightph.setIcon(fightingph);
		fightph.setBounds(100, 30, 100, 70);
		mph=new JLabel();
		mph.setSize(32,32);
		mph.setIcon(new ImageIcon(getClass().getResource("/res/image/Master/"+(FightingMonster.ID)+".png")));
		mph.setBounds(163+50, 25, 32, 32);
		matk=new JLabel("攻:"+FightingMonster.atk);
		matk.setBounds(163+85,28, 80, 20);
		mdef=new JLabel("防:"+FightingMonster.def);
		mdef.setBounds(163+85,60, 80, 20);
		mhp=new JLabel("血量:"+FightingMonster.hp);
		mhp.setBounds(163+50,100, 100, 20);
		mhp.setFont(new Font("宋体",Font.BOLD,18));
		mhp.setForeground(Color.green);
		fig=new JDialog(mainframe," ",false);
		fig.setSize(300,150);
		fig.setLocation(mainframe.getLocation().x+100,mainframe.getLocation().y+140);
		fig.setUndecorated(true);
		showfighting=new JPanel();
		showfighting.setLayout(null);
		showfighting.setBackground(Color.gray);
		showfighting.add(tip);
		showfighting.add(roleph);
		showfighting.add(roleatk);
		showfighting.add(roledef);
		showfighting.add(rolehp);
		showfighting.add(fightph);
		showfighting.add(mph);
		showfighting.add(matk);
		showfighting.add(mdef);
		showfighting.add(mhp);
		fig.add(showfighting);
		fig.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {
				
			}
			public void keyReleased(KeyEvent arg0) {
				
			}
			public void keyPressed(KeyEvent arg0) {
				switch(arg0.getKeyCode()) {
				case 1000000:break;
				default:
				do{
					if(role.def<FightingMonster.atk) {
						role.hp-=FightingMonster.atk-role.def;
						hp.setText(""+role.hp);
						FightingMonster.hp-=role.atk-FightingMonster.def;
					}
					else {
						Fighting=false;
						check=false;
						showMes.setText("你战胜了"+FightingMonster.name+",获得"+FightingMonster.meney+"金币");
						role.money+=FightingMonster.meney;
						mon.setText(""+role.money);
						System.out.println("你获得了"+FightingMonster.meney+"金币");
						map[floor][role.y][role.x]=0;
						PlaySoundofFightOver();
						if(floor==10 && FightingMonster.ID==28) {
							map[10][10][10]=0;
						}
						fig.setVisible(false);
						repaint();
						break;
					}
				}while(role.hp>=0 && FightingMonster.hp>=0) ;
				}
			}
		});
		fig.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {
				check=false;
				Fighting=false;
				fig.setVisible(false);
				if(floor==10 && FightingMonster.ID==28) {
					map[10][10][10]=0;
				}
				repaint();
			}
			public void mousePressed(MouseEvent arg0) {
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mouseEntered(MouseEvent arg0) {
			}
			public void mouseClicked(MouseEvent arg0) {
			}
		});
	}

	private void loadImage() {//载入图像
    	ImageIcon icon=new ImageIcon(getClass().getResource("/res/image/Characters/roleaction.png"));
    	role.ph=icon.getImage();//主角
    	icon=new ImageIcon(getClass().getResource("/res/image/wall/floor1.png"));
    	floor1Image=icon.getImage();//地板1
    	icon=new ImageIcon(getClass().getResource("/res/image/wall/wall1.png"));
    	wall1Image=icon.getImage();//墙1
    	icon=new ImageIcon(getClass().getResource("/res/image/wall/floor2.png"));
    	floor2Image=icon.getImage();//地板2
    	icon=new ImageIcon(getClass().getResource("/res/image/wall/wall2.png"));
    	wall2Image=icon.getImage();//墙2
    	icon=new ImageIcon(getClass().getResource("/res/image/wall/floor3.png"));
    	floor3Image=icon.getImage();//地板3
    	icon=new ImageIcon(getClass().getResource("/res/image/wall/wall3.png"));
    	wall3Image=icon.getImage();//墙3
    	icon=new ImageIcon(getClass().getResource("/res/image/wall/wall4.png"));
    	wall4Image=icon.getImage();//墙4
    	icon=new ImageIcon(getClass().getResource("/res/image/wall/wall5.png"));
    	wall5Image=icon.getImage();//墙5
    	icon=new ImageIcon(getClass().getResource("/res/image/wall/wall6.png"));
    	wall6Image=icon.getImage();//墙6
    	icon=new ImageIcon(getClass().getResource("/res/image/wall/wall7.png"));
    	wall7Image=icon.getImage();//墙7
    	icon=new ImageIcon(getClass().getResource("/res/image/wall/wall8.png"));
    	wall8Image=icon.getImage();//墙8
    	
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/14.png"));
    	for(int o=0;o<14;o++) {
    		goodph[o]=icon;
    	}
    	goodph[14]=icon;
    	m[14].ph=icon.getImage();//怪14
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/15.png"));
    	goodph[15]=icon;
    	m[15].ph=icon.getImage();//怪15
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/16.png"));
    	goodph[16]=icon;
    	m[16].ph=icon.getImage();//怪16
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/17.png"));
    	goodph[17]=icon;
    	m[17].ph=icon.getImage();//怪17
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/18.png"));
    	goodph[18]=icon;
    	m[18].ph=icon.getImage();//怪18
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/19.png"));
    	goodph[19]=icon;
    	m[19].ph=icon.getImage();//怪19
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/20.png"));
    	goodph[20]=icon;
    	m[20].ph=icon.getImage();//怪20
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/21.png"));
    	goodph[21]=icon;
    	m[21].ph=icon.getImage();//怪21
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/22.png"));
    	goodph[22]=icon;
    	m[22].ph=icon.getImage();//怪22
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/23.png"));
    	goodph[23]=icon;
    	m[23].ph=icon.getImage();//怪23
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/24.png"));
    	goodph[24]=icon;
    	m[24].ph=icon.getImage();//怪24
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/25.png"));
    	goodph[25]=icon;
    	m[25].ph=icon.getImage();//怪25
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/26.png"));
    	goodph[26]=icon;
    	m[26].ph=icon.getImage();//怪26
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/27.png"));
    	goodph[27]=icon;
    	m[27].ph=icon.getImage();//怪27
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/28.png"));
    	goodph[28]=icon;
    	m[28].ph=icon.getImage();//怪28
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/29.png"));
    	goodph[29]=icon;
    	m[29].ph=icon.getImage();//怪29
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/30.png"));
    	goodph[30]=icon;
    	m[30].ph=icon.getImage();//怪30
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/31.png"));
    	goodph[31]=icon;
    	m[31].ph=icon.getImage();//怪31
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/32.png"));
    	goodph[32]=icon;
    	m[32].ph=icon.getImage();//怪32
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/33.png"));
    	goodph[33]=icon;
    	m[33].ph=icon.getImage();//怪33
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/34.png"));
    	goodph[34]=icon;
    	m[34].ph=icon.getImage();//怪34
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/35.png"));
    	goodph[35]=icon;
    	m[35].ph=icon.getImage();//怪35
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/36.png"));
    	goodph[36]=icon;
    	m[36].ph=icon.getImage();//怪36
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/37.png"));
    	goodph[37]=icon;
    	m[37].ph=icon.getImage();//怪37
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/38.png"));
    	goodph[38]=icon;
    	m[38].ph=icon.getImage();//怪38
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/39.png"));
    	goodph[39]=icon;
    	m[39].ph=icon.getImage();//怪39
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/40.png"));
    	goodph[40]=icon;
    	m[40].ph=icon.getImage();//怪40
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/41.png"));
    	goodph[41]=icon;
    	m[41].ph=icon.getImage();//怪41
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/42.png"));
    	goodph[42]=icon;
    	m[42].ph=icon.getImage();//怪42
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/43.png"));
    	goodph[43]=icon;
    	m[43].ph=icon.getImage();//怪43
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/44.png"));
    	goodph[44]=icon;
    	m[44].ph=icon.getImage();//怪44
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/45.png"));
    	goodph[45]=icon;
    	m[45].ph=icon.getImage();//怪45
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/46.png"));
    	goodph[46]=icon;
    	m[46].ph=icon.getImage();//怪46
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/47.png"));
    	goodph[47]=icon;
    	m[47].ph=icon.getImage();//怪47
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/48.png"));
    	goodph[48]=icon;
    	m[48].ph=icon.getImage();//怪48
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/49.png"));
    	goodph[49]=icon;
    	m[49].ph=icon.getImage();//怪9
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/50.png"));
    	goodph[50]=icon;
    	m[50].ph=icon.getImage();//怪50
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/51.png"));
    	goodph[51]=icon;
    	m[51].ph=icon.getImage();//怪51
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/52.png"));
    	goodph[52]=icon;
    	m[52].ph=icon.getImage();//怪52
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/53.png"));
    	goodph[53]=icon;
    	m[53].ph=icon.getImage();//怪53
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/54.png"));
    	goodph[54]=icon;
    	m[54].ph=icon.getImage();//怪54
    	icon=new ImageIcon(getClass().getResource("/res/image/Master/55.png"));
    	goodph[55]=icon;
    	m[55].ph=icon.getImage();//怪55
    	
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/101.png"));
    	shops.ph=icon.getImage();//金币商店
    	
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/102.png"));
    	keys[0].ph=icon.getImage();//黄钥匙
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/103.png"));
    	keys[1].ph=icon.getImage();//蓝钥匙
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/104.png"));
    	keys[2].ph=icon.getImage();//红钥匙
    	
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/107.png"));
    	drugs[0].ph=icon.getImage();//红药
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/108.png"));
    	drugs[1].ph=icon.getImage();//蓝药
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/109.png"));
    	drugs[2].ph=icon.getImage();//绿药
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/110.png"));
    	drugs[3].ph=icon.getImage();//黄药
    	
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/105.png"));
    	jewels[0].ph=icon.getImage();//红宝石
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/106.png"));
    	jewels[1].ph=icon.getImage();//蓝宝石
    	
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/201.png"));
    	doors[0].ph=icon.getImage();//黄门
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/202.png"));
    	doors[1].ph=icon.getImage();//蓝门
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/203.png"));
    	doors[2].ph=icon.getImage();//红门
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/204.png"));
    	doors[3].ph=icon.getImage();//机关门
    	
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/400.png"));
    	goods[0].ph=icon.getImage();//怪物手册
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/401.png"));
    	goods[1].ph=icon.getImage();//飞行器
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/402.png"));
    	goods[2].ph=icon.getImage();//稿
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/403.png"));
    	goods[3].ph=icon.getImage();//暂存
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/404.png"));
    	goods[4].ph=icon.getImage();
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/405.png"));
    	goods[5].ph=icon.getImage();
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/406.png"));
    	goods[6].ph=icon.getImage();
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/407.png"));
    	goods[7].ph=icon.getImage();
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/408.png"));
    	goods[8].ph=icon.getImage();
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/409.png"));
    	goods[9].ph=icon.getImage();
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/410.png"));
    	goods[10].ph=icon.getImage();
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/120.png"));
    	goods[11].ph=icon.getImage();
    	
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/411.png"));
    	swords[0].ph=icon.getImage();//
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/412.png"));
    	swords[1].ph=icon.getImage();//

    	icon=new ImageIcon(getClass().getResource("/res/image/Items/414.png"));
    	swords[3].ph=icon.getImage();//
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/415.png"));
    	swords[4].ph=icon.getImage();//
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/416.png"));
    	swords[5].ph=icon.getImage();//
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/417.png"));
    	swords[6].ph=icon.getImage();//
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/418.png"));
    	swords[7].ph=icon.getImage();//
    	
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/419.png"));
    	shields[0].ph=icon.getImage();//
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/420.png"));
    	shields[1].ph=icon.getImage();//
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/422.png"));
    	shields[3].ph=icon.getImage();//
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/423.png"));
    	shields[4].ph=icon.getImage();//
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/424.png"));
    	shields[5].ph=icon.getImage();//
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/425.png"));
    	shields[6].ph=icon.getImage();//
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/426.png"));
    	shields[7].ph=icon.getImage();//
    	
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/501.png"));
    	up.ph=icon.getImage();//
    	icon=new ImageIcon(getClass().getResource("/res/image/Items/500.png"));
    	down.ph=icon.getImage();//
    	
    	icon=new ImageIcon(getClass().getResource("/res/image/Events/800.png"));
    	npc[0].ph=icon.getImage();
    	icon=new ImageIcon(getClass().getResource("/res/image/Events/801.png"));
    	npc[1].ph=icon.getImage();
    	icon=new ImageIcon(getClass().getResource("/res/image/Events/802.png"));
    	npc[2].ph=icon.getImage();
    	icon=new ImageIcon(getClass().getResource("/res/image/Events/803.png"));
    	npc[3].ph=icon.getImage();
    	icon=new ImageIcon(getClass().getResource("/res/image/Events/804.png"));
    	npc[4].ph=icon.getImage();
    	icon=new ImageIcon(getClass().getResource("/res/image/Events/805.png"));
    	npc[5].ph=icon.getImage();	
	}
    
	private void CreateMonster() {
		for(int index=0;index<56;index++) {
			if(index<14) CreateMonster(14);
			else CreateMonster(index);
		}
	}
	
	private void CreateMonster(int index) {
		switch(index) {
		case 14:m[14]=new Monster("兽人",240, 28, 5, 3, 1,14);break;
		case 15:m[15]=new Monster("绿史莱姆",50, 18, 1, 1, 2,15);break;
		case 16:m[16]=new Monster("红史莱姆",60, 22, 5, 2, 2,16);break;
		case 17:m[17]=new Monster("黑史莱姆",90,36,8,3,3,17);break;
		case 18:m[18]=new Monster("小蝙蝠",  22, 45, 2, 3, 3,18);break;
		case 19:m[19]=new Monster("骷髅人",110, 38, 3, 3, 3,19);break;
		case 20:m[20]=new Monster("透明史莱姆",200, 35, 10, 4, 5,20);break;
		case 21:m[21]=new Monster("骷髅兵",200, 58, 15, 5, 6,21);break;
		case 22:m[22]=new Monster("骷髅队长",400,60,30,15,8,22);break;
		case 23:m[23]=new Monster("大蝙蝠",200,48,18,10,9,23);break;
		case 24:m[24]=new Monster("幽灵",600,59,21,11,10,24);break;
		case 25:m[25]=new Monster("死神奴仆",355,60,18,12,11,25);break;
		case 26:m[26]=new Monster("石头脸",800,52,45,11,12,26);break;
		case 27:m[27]=new Monster("史莱姆队长",200,70,12,12,13,27);break;
		case 28:m[28]=new Monster("骷髅王",1000,80,55,100,15,28);break;
		case 29:m[29]=new Monster("低级守卫",400,68,35,12,17,29);break;
		case 30:m[30]=new Monster("高级守卫",2000,150,90,45,19,30);break;
		case 31:m[31]=new Monster("红蝙蝠",20,1,2,2,22,31);break;
		case 32:m[32]=new Monster("红骷髅兵",900,180,80,50,1,32);break;
		case 33:m[33]=new Monster("祭司",20,1,2,2,1,33);break;
		case 34:m[34]=new Monster("骷髅皇 ",1500,200,120,400,1,34);break;
		case 35:m[35]=new Monster("低级守卫队长",1200,180,90,50,1,35);break;
		case 36:m[36]=new Monster("中级守卫队长",1800,220,150,80,1,36);break;
		case 37:m[37]=new Monster("大章鱼",20,1,2,2,1,37);break;
		case 38:m[38]=new Monster("兵人",20,1,2,2,1,38);break;
		case 39:m[39]=new Monster("见习法师",20,1,2,2,1,39);break;
		case 40:m[40]=new Monster("双剑人",20,1,2,2,1,40);break;
		case 41:m[41]=new Monster("低级法师",20,1,2,2,1,41);break;
		case 42:m[42]=new Monster("中级法师",20,1,2,2,1,42);break;
		case 43:m[43]=new Monster("红骑士",20,1,2,2,1,43);break;
		case 44:m[44]=new Monster("黑骑士",20,1,2,2,1,44);break;
		case 45:m[45]=new Monster("兽人兵",20,1,2,2,1,45);break;
		case 46:m[46]=new Monster("屠龙者",20,1,2,2,1,46);break;
		case 47:m[47]=new Monster("守卫",20,1,2,2,1,47);break;
		case 48:m[48]=new Monster("魔王",20,1,2,2,1,48);break;
		case 49:m[49]=new Monster("大法师",20,1,2,2,1,49);break;
		case 50:m[50]=new Monster("魔龙",20,1,2,2,1,50);break;
		case 51:m[51]=new Monster("幽灵法师",20,1,2,2,1,51);break;
		case 52:m[52]=new Monster("黑幽灵法师",20,1,2,2,1,52);break;
		case 53:m[53]=new Monster("黄骷髅",20,1,2,2,1,53);break;
		case 54:m[54]=new Monster("红幽灵法师",20,1,2,2,1,54);break;
		case 55:m[55]=new Monster("魔王",10000,500,250,10000,1,55);break;
		}
	}

	public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	drawMap(g);
    	drawAttribute(g);
    	try {
			drawRole(g);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	//创建属性界面的方法
	private void drawAttribute(Graphics g) {
		//构造背景
		for(int i=0;i<15;i++) {
			for(int j=14;j<=21;j++) {
				if(i==0 || i==14 || j==14 || j==21) {
					g.drawImage(wall1Image,j*CS,i*CS,this);
				}else {
					g.drawImage(floor1Image,j*CS,i*CS,this);
				}
			}
		}
		hp.setText(""+role.hp);
		atk.setText(""+role.atk);
		def.setText(""+role.def);
		mon.setText(""+role.money);
		ykey.setText(""+role.ykey);
		bkey.setText(""+role.bkey);
		rkey.setText(""+role.rkey);
		for(int index=0;index<3;index++) {
			if(goods[index].ex==true) {
				wp[index].setText("");
			}else {
				wp[index].setText("X");
			}
		}
		
	}

	private void CreateGoods() {
		keys[0]=new key("黄钥匙",102);
		keys[1]=new key("蓝钥匙",103);
		keys[2]=new key("红钥匙",104);
		
		drugs[0]=new drug("红药水", 100,107);
		drugs[1]=new drug("蓝药水", 300,108);
		drugs[2]=new drug("绿药水", 600,109);
		drugs[3]=new drug("黄药水", 1000,110);
		
		doors[0]=new door("黄门",201);
		doors[1]=new door("蓝门",202);
		doors[2]=new door("红门",203);
		doors[3]=new door("机关门",204);
		
		jewels[0]=new jewel("红宝石", 105);
		jewels[1]=new jewel("蓝宝石", 106);
		
		goods[0]=new Goods("怪物手册",400);
		goods[1]=new Goods("飞行器",401);
		goods[2]=new Goods("稿",402);
		goods[3]=new Goods("1",403);
		goods[4]=new Goods("1",404);
		goods[5]=new Goods("1",405);
		goods[6]=new Goods("1",406);
		goods[7]=new Goods("1",407);
		goods[8]=new Goods("1",408);
		goods[9]=new Goods("1",409);
		goods[10]=new Goods("",410);
		goods[11]=new Goods("",120);
		
		swords[0]=new sword("铁剑",8,411);
		swords[1]=new sword("青铜剑", 15,412);
		swords[2]=new sword("金剑", 20,413);
		swords[3]=new sword("铂金剑", 25,414);
		swords[4]=new sword("钻石剑", 40,415);
		swords[5]=new sword("圣剑", 65,416);
		swords[6]=new sword("神圣剑", 80,417);
		swords[7]=new sword("渊虹", 500,418);
		
		shields[0]=new shield("铁盾", 10,419);
		shields[1]=new shield("青铜盾", 18,420);
		shields[2]=new shield("银盾", 22,421);
		shields[3]=new shield("金盾", 28,422);
		shields[4]=new shield("铂金盾", 42,423);
		shields[5]=new shield("钻石盾", 68,424);
		shields[6]=new shield("王者盾", 88,425);
		shields[7]=new shield("圣盾", 300,426);
		
		npc[0]=new NPC("大法师",800);
		npc[1]=new NPC("被封印的勇士",801);
		npc[2]=new NPC("公主",802);
		npc[3]=new NPC("祭司",803);
		npc[4]=new NPC("智者",804);
		npc[5]=new NPC("商人",805);
	}

	private void drawMap(Graphics g) {
		for(int i=0;i<this.ROW;i++) {
			for(int j=0;j<this.COL;j++) {
				if(map[floor][i][j]<9) {
					switch(map[floor][i][j]) {
						case 0://map的标记为0时画出地板1
							if(floor>=0 && floor<=10) {
								g.drawImage(floor1Image,j*CS,i*CS, this);
								break;
							}else if(floor>10 && floor<=20){
								g.drawImage(floor2Image,j*CS,i*CS, this);
								break;
							}else {
								g.drawImage(floor3Image,j*CS,i*CS, this);
								break;
							}
						case 1: //map的标记为1时画出墙1
							g.drawImage(wall1Image,j*CS,i*CS, this);
							break;
						case 2: 
							g.drawImage(wall2Image,j*CS,i*CS, this);
							break;
						case 3: 
							g.drawImage(wall3Image,j*CS,i*CS, this);
							break;
						case 4: 
							g.drawImage(wall4Image,j*CS,i*CS, this);
							break;
						case 5: 
							g.drawImage(wall5Image,j*CS,i*CS, this);
							break;
						case 6: 
							g.drawImage(wall6Image,j*CS,i*CS, this);
							break;
						case 7: 
							g.drawImage(wall7Image,j*CS,i*CS, this);
							break;
						case 8:
							g.drawImage(wall8Image,j*CS,i*CS, this);
							break;
						default:
							break;
					}
				}
				else if(map[floor][i][j]>13 && map[floor][i][j]<56){
						g.drawImage(floor1Image,j*CS,i*CS, this);
						CreateMonster(map[floor][i][j]);
						m[map[floor][i][j]].ph=goodph[map[floor][i][j]].getImage();
						g.drawImage(m[map[floor][i][j]].ph,j*CS,i*CS, this);
				}else if(map[floor][i][j]>=102 && map[floor][i][j]<=104){
					g.drawImage(floor1Image,j*CS,i*CS, this);
					g.drawImage(keys[map[floor][i][j]-102].ph,j*CS,i*CS, this);
				}else if(map[floor][i][j]>=107 && map[floor][i][j]<=110){
					g.drawImage(floor1Image,j*CS,i*CS, this);
					g.drawImage(drugs[map[floor][i][j]-107].ph,j*CS,i*CS, this);
				}else if(map[floor][i][j]>=201 && map[floor][i][j]<=204){
					g.drawImage(floor1Image,j*CS,i*CS, this);
					g.drawImage(doors[map[floor][i][j]-201].ph,j*CS,i*CS, this);
				}else if(map[floor][i][j]>=400 && map[floor][i][j]<=410){
					g.drawImage(floor1Image,j*CS,i*CS, this);
					g.drawImage(goods[map[floor][i][j]-400].ph,j*CS,i*CS, this);
				}else if(map[floor][i][j]==120){
					g.drawImage(floor1Image,j*CS,i*CS, this);
					g.drawImage(goods[11].ph,j*CS,i*CS, this);
				}else if(map[floor][i][j]>=411 && map[floor][i][j]<=418){
					g.drawImage(floor1Image,j*CS,i*CS, this);
					g.drawImage(swords[map[floor][i][j]-411].ph,j*CS,i*CS, this);
				}else if(map[floor][i][j]>=419 && map[floor][i][j]<=426){
					g.drawImage(floor1Image,j*CS,i*CS, this);
					g.drawImage(shields[map[floor][i][j]-419].ph,j*CS,i*CS, this);
				}else if(map[floor][i][j]==101) {
					g.drawImage(floor1Image,j*CS,i*CS, this);
					g.drawImage(shops.ph,j*CS,i*CS, this);
				}else if(map[floor][i][j]==105) {
					g.drawImage(floor1Image,j*CS,i*CS, this);
					g.drawImage(jewels[0].ph,j*CS,i*CS, this);
				}else if(map[floor][i][j]==106) {
					g.drawImage(floor1Image,j*CS,i*CS, this);
					g.drawImage(jewels[1].ph,j*CS,i*CS, this);
				}else if(map[floor][i][j]==501) {
					g.drawImage(floor1Image,j*CS,i*CS, this);
					g.drawImage(up.ph,j*CS,i*CS, this);
				}else if(map[floor][i][j]==500) {
					g.drawImage(floor1Image,j*CS,i*CS, this);
					g.drawImage(down.ph,j*CS,i*CS, this);
				}else if(map[floor][i][j]>=800 && map[floor][i][j]<=806) {
					g.drawImage(floor1Image,j*CS,i*CS, this);
					g.drawImage(npc[map[floor][i][j]-800].ph,j*CS,i*CS, this);
				}
			}
		}
	}
	
	private void drawRole(Graphics g) throws Exception {//角色的位置,这个位置原来有块[地板]的。
		if(count==3) g.drawImage(role.ph,x*CS,y*CS, x*CS+CS, y*CS+CS,
				0, count*CS+2, CS,CS+count*CS,this);
		else if(count==2)  g.drawImage(role.ph,x*CS,y*CS, x*CS+CS, y*CS+CS,
                0, count*CS+2, CS,CS+count*CS,this);
		else  g.drawImage(role.ph,x*CS,y*CS, x*CS+CS, y*CS+CS,
                0, count*CS, CS,CS+count*CS,this);
		role.x=x;
		role.y=y;
		System.out.println(role.x+","+role.y);
		this.events();
	}
	
	private void events(){//获得物品
		switch(map[floor][role.y][role.x]) {
			case 102://得到黄钥匙
				map[floor][role.y][role.x]=0;
				role.ykey++;
				PlaySoundofGoods();
				showMes.setText("获得黄钥匙一把!");
				repaint();
				break;
			case 103://得到蓝钥匙
				map[floor][role.y][role.x]=0;
				role.bkey++;
				PlaySoundofGoods();
				showMes.setText("获得蓝钥匙一把!");
				repaint();
				break;
			case 104://得到红钥匙
				map[floor][role.y][role.x]=0;
				role.rkey++;
				PlaySoundofGoods();
				showMes.setText("获得红钥匙一把!");
				repaint();
				break;
			case 105://得到红宝石
				map[floor][role.y][role.x]=0;
				role.atk+=floor/10+1;//效果随楼层(每10层)数增加
				showMes.setText("获得红宝石!");
				PlaySoundofGoods();
				repaint();
				break;
			case 106://得到蓝宝石
				map[floor][role.y][role.x]=0;
				role.def+=floor/10+1;
				PlaySoundofGoods();
				showMes.setText("获得蓝宝石!");
				repaint();
				break;
			case 107://获得血瓶
			case 108:
			case 109:
			case 110:
				System.out.println(map[floor][role.y][role.x]);
				role.hp+=drugs[map[floor][role.y][role.x]-107].hp;
				System.out.println(role.hp);
				map[floor][role.y][role.x]=0;
				PlaySoundofGoods();
				showMes.setText("获得血瓶!");
				repaint();
				break;
			case 400:
			case 401:
			case 402:
			case 403:
			case 404:
			case 405:
			case 406:
			case 407:
			case 408:
			case 409:
			case 410:
				System.out.println(map[floor][role.y][role.x]);
				goods[map[floor][role.y][role.x]-400].ex=true;
				System.out.println(goods[map[floor][role.y][role.x]-400].ex);
				showMes.setText("获得:"+goods[map[floor][role.y][role.x]-400].name+"!");
				map[floor][role.y][role.x]=0;
				PlaySoundofGoods();
				repaint();
				break;
			case 120://获得大钥匙
				System.out.println(map[floor][role.y][role.x]);
				goods[map[floor][role.y][role.x]-109].ex=true;
				System.out.println(goods[map[floor][role.y][role.x]-109].ex);
				showMes.setText("获得:"+goods[map[floor][role.y][role.x]-109].name+"!");
				map[floor][role.y][role.x]=0;
				PlaySoundofGoods();
				repaint();
				break;
			case 411://获得武器
			case 412:
			case 413:
			case 414:
			case 415:
			case 416:
			case 417:
			case 418:
				System.out.println(map[floor][role.y][role.x]);
				role.atk+=swords[map[floor][role.y][role.x]-411].atk;
				showatk.setIcon(new ImageIcon(getClass().getResource("/res/image/Items/"+map[floor][role.y][role.x]+".png")));
				showMes.setText("获得:"+swords[map[floor][role.y][role.x]-411].name+"!");
				map[floor][role.y][role.x]=0;
				PlaySoundofGoods();
				repaint();
				break;
			case 419:
			case 420:
			case 421:
			case 422:
			case 423:
			case 424:
			case 425:
			case 426:
				System.out.println(map[floor][role.y][role.x]);
				role.def+=shields[map[floor][role.y][role.x]-419].def;
				showdef.setIcon(new ImageIcon(getClass().getResource("/res/image/Items/"+map[floor][role.y][role.x]+".png")));
				showMes.setText("获得:"+shields[map[floor][role.y][role.x]-419].name+"!");
				map[floor][role.y][role.x]=0;
				PlaySoundofGoods();
				repaint();
				break;
		}
	}
	
	private void meetNpc(int ID) {
		dialogc=new JDialog(mainframe, null, true);
		String s=null;
		ImageIcon photo = null;
		JPanel dialogp=new JPanel(null);
		JLabel pict=new JLabel();
		JLabel name=new JLabel(npc[ID-800].name);
		JTextArea content=new JTextArea();
		content.setBorder(BorderFactory.createLineBorder(Color.white));
		JLabel tipc=new JLabel("(按任意键关闭对话)");
		switch(ID) {
			case 800:
				photo=new ImageIcon(getClass().getResource("/res/image/Events/800.png"));
				s=string[floor-1][1];
				disp=true;
				break;
			case 802:
				photo=new ImageIcon(getClass().getResource("/res/image/Events/802.png"));
				s=string[floor-1][2];
				break;
			case 803:
				photo=new ImageIcon(getClass().getResource("/res/image/Events/803.png"));
				s=string[floor-1][3];
				break;
			case 804:
				photo=new ImageIcon(getClass().getResource("/res/image/Events/804.png"));
				s=string[floor-1][4];
				break;
		}
		dialogc.setSize(350,250);
		dialogc.setUndecorated(true);
		content.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {
			}
			public void keyReleased(KeyEvent arg0) {
				switch(arg0.getKeyCode()) {
				case 1000000:break;
				default:
					dialogc.setVisible(false);
					break;
				}
			}
			public void keyPressed(KeyEvent arg0) {
				
			}
		});
		content.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {
				dialogc.setVisible(false);
			}
			public void mousePressed(MouseEvent arg0) {
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mouseEntered(MouseEvent arg0) {	
			}
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		dialogp.setSize(350,250);
		dialogp.setBackground(Color.black);
		pict.setIcon(photo);
		pict.setBounds(5, 5, 40, 40);
		name.setBounds(50, 20, 100, 25);
		name.setBackground(Color.white);
		name.setForeground(Color.white);
		content.setText(s);
		content.setLineWrap(true);
		content.setEditable(false);
		content.setBounds(15, 50, 305, 170);
		content.setFont(new Font("宋体",Font.BOLD, 16));
		content.setBackground(Color.black);
		content.setForeground(Color.WHITE);
		tipc.setBounds(110,225, 120, 20);
		tipc.setForeground(Color.white);
		tipc.setBackground(Color.white);
		dialogp.add(pict);
		dialogp.add(name);
		dialogp.add(content);
		dialogp.add(tipc);
		dialogc.setLocation(mainframe.getLocation().x+80,mainframe.getLocation().y+100);
		dialogc.add(dialogp);
		dialogc.setVisible(true);
	}

	public String monsterharm(Monster mons) {
		String harm="无法估算";
		int hp=mons.hp;
		int mohp=mons.hp;
		int rolehp=role.hp;
		if(role.atk>mons.def) {
			if(role.def<mons.atk) {
				do {
					rolehp=rolehp-(mons.atk-role.def);
					mohp=mohp-(role.atk-mons.def);
				}while(mohp>=0);
				return String.valueOf(role.hp-rolehp);
			}else {
				return "0";
			}
		}else {
			return harm;
		}
	}
	
	public void creatSound(){
		PlaySounds ps=new PlaySounds(soundofgoods);
		threadofgoods=new Thread(ps);
		ps=new PlaySounds(soundofdoors);
		threadofdoors=new Thread(ps);
		ps=new PlaySounds(soundoffight);
		threadoffight=new Thread(ps);
		ps=new PlaySounds(soundoffightover);
		threadoffightover=new Thread(ps);
	}
	
	private void PlaySoundofGoods() {//获得钥匙声音
		threadofgoods.start();
		InputStream soundofgoods=this.getClass().getResourceAsStream("/res/Audio/SE/get.mp3");
		PlaySounds ps=new PlaySounds(soundofgoods);
		threadofgoods=new Thread(ps);
	}
	
	private void PlaySoundofDoor() {//门的声音
		threadofdoors.start();
		InputStream soundofdoors=this.getClass().getResourceAsStream("/res/Audio/SE/door.mp3");
		PlaySounds ps=new PlaySounds(soundofdoors);
		threadofdoors=new Thread(ps);
	}
	
	private void PlaySoundofFight() {
		threadoffight.start();
		InputStream soundoffight=this.getClass().getResourceAsStream("/res/Audio/SE/fight.mp3");
		PlaySounds ps=new PlaySounds(soundoffight);
		threadoffight=new Thread(ps);
	}

	private void PlaySoundofFightOver() {
		threadoffightover.start();
		InputStream soundoffightover=this.getClass().getResourceAsStream("/res/Audio/SE/fightover.mp3");
		PlaySounds ps=new PlaySounds(soundoffightover);
		threadoffightover=new Thread(ps);
	}

	public void keyPressed(KeyEvent arg0) {
		
	}
	public void keyReleased(KeyEvent arg0) {
		int keyCode=arg0.getKeyCode();
		switch(keyCode) {
			case KeyEvent.VK_LEFT:
				count=LEFT;
				move(LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				count=RIGHT;
				move(RIGHT);
				break;
			case KeyEvent.VK_UP:
				count=UP;
				move(UP);
				break;
			case KeyEvent.VK_DOWN:
				count=DOWN;
				move(DOWN);
				break;
		}
		this.repaint();
	}
	public void keyTyped(KeyEvent arg0) {
		
	}
    private boolean isAllow(int i,int j) {//判断是否能移动
    	if((map[floor][j][i]>0 && map[floor][j][i]<10) || 
    			(map[floor][j][i]>200 && map[floor][j][i]<204)) {
    		if(map[floor][j][i]>0 && map[floor][j][i]<10){
    			showMes.setText("前面是墙!");
    			return false;
    		}
    		if(doors[map[floor][j][i]-201].ID==201 && role.ykey>0) {
    			System.out.println(role.ykey);
    			role.ykey--;
    			PlaySoundofDoor();
    			map[floor][j][i]=0;
    			System.out.println(role.ykey);
    			showMes.setText("开了张黄门");
    			return false;
    		}else if(doors[map[floor][j][i]-201].ID==202 && role.bkey>0) {
    			System.out.println(role.bkey);
    			role.bkey--;
    			PlaySoundofDoor();
    			map[floor][j][i]=0;
    			System.out.println(role.bkey);
    			showMes.setText("开了张蓝门");
    			return false;
    		}else if(doors[map[floor][j][i]-201].ID==203 && role.rkey>0) {
    			System.out.println(role.rkey);
    			role.rkey--;
    			PlaySoundofDoor();
    			map[floor][j][i]=0;
    			System.out.println(role.rkey);
    			showMes.setText("开了张红门");
    			return false;
    		}
    		else {
    			return false;
    		}
    	}else if((map[floor][j][i]>=14 && map[floor][j][i]<=60) ) {
    		if(map[10][j][i]==28) {//第10层Boos事件
    			int index=0;
    			for(int n=1;n<14;n++) {
    				for(int m=1;m<14;m++) {
    					if(map[floor][m][n]==21 || map[floor][m][n]==22) {
    						index++;
    					}
    				}
    			}
    			if(index!=0) {
    				JDialog tip=new JDialog(mainframe, null, true);
					tip.setUndecorated(true);
					tip.setSize(290, 40);
					JTextArea t=new JTextArea("你必须打败我的手下,哈哈哈");
					t.setBackground(Color.black);
					t.setForeground(Color.white);
					t.setEditable(false);
					t.setFont(new Font("宋体",Font.BOLD, 30));
					t.addKeyListener(new KeyListener() {
						public void keyTyped(KeyEvent arg0) {}
						public void keyReleased(KeyEvent arg0) {
							switch (arg0.getKeyCode()) {
								case 1000:break;
								default:
									tip.setVisible(false);
							}
						}
						public void keyPressed(KeyEvent arg0) {}
					});
					tip.setLocation(mainframe.getLocation().x+100,mainframe.getLocation().y+180);
					tip.add(t);
					tip.setVisible(true);
					repaint();
					return false;
    			}
    		}
    		if(role.atk>m[map[floor][j][i]].def) {
    			FightingMonster=m[map[floor][j][i]];
    			showFig();
    			System.out.println(FightingMonster.ID);
    			check=true;
    			Fighting=true;
    			new Thread() {
    				public void run(){
    					fig.setVisible(true);
    					fig.setFocusable(true);
    					do{
    						try {
								if(Fighting) {//战斗
									PlaySoundofFight();
									Thread.currentThread().sleep(300);
									if(FightingMonster.atk-role.def>0) {
										role.hp-=FightingMonster.atk-role.def;
										hp.setText(""+role.hp);
										rolehp.setText("血:"+role.hp);
										FightingMonster.hp-=(role.atk-FightingMonster.def);
										mhp.setText("血:"+FightingMonster.hp);
									}else {
										FightingMonster.hp-=role.atk-FightingMonster.def;
										mhp.setText("血:"+FightingMonster.hp);
									}
    							}else {
    								do{
    									if(FightingMonster.atk-role.def>=0) {
    										role.hp-=FightingMonster.atk-role.def;
    										hp.setText(""+role.hp);
    										FightingMonster.hp-=role.atk-FightingMonster.def;
    									}else {
    										FightingMonster.hp-=role.atk-FightingMonster.def;
    									}
    								}while(role.hp>=0 && FightingMonster.hp>=0) ;
    								
    							}
								
								if(role.hp<=0) {//游戏结束
									Fighting=false;
									fig.setVisible(false);
									GameOver();
									System.out.println("你输了,游戏结束!");
									Thread.currentThread().stop();
								}
								if(FightingMonster.hp<=0) {//战斗结束
									Fighting=false;
									fig.setVisible(false);
									showMes.setText("你战胜了"+FightingMonster.name+",获得"+FightingMonster.meney+"金币");
									System.out.println("你获得了"+FightingMonster.meney+"金币");
									role.money+=FightingMonster.meney;
									mon.setText(""+role.money);
									map[floor][j][i]=0;
									check=false;
									PlaySoundofFightOver();
									if(floor==10 && FightingMonster.ID==28) {
										map[10][10][10]=0;
									}
									repaint();
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
    					}while(check);
    					System.gc();
    				}

    			}.start();
    		} else{
    			showMes.setText("你太渣了,攻击力太低了!");
    			return false;
    		}
    	}else if(map[floor][j][i]==shops.ID) {
			showShop();
			return false;
		}else if(map[floor][j][i]==204){//机关门开启的条件
			boolean open=false;
			for(int k=0;k<15;k++) {
				for(int g=0;g<15;g++) {
					if(map[floor][g][k]==29 || map[floor][g][k]==30 ||
							map[floor][g][k]==35 || map[floor][g][k]==36) {
						open=true;
					}	
				}
			}
			if(!open) {
				map[floor][j][i]=0;
				showMes.setText("机关门开启");
				if(floor==10) {
					map[10][10][10]=1;
					JDialog tip=new JDialog(mainframe, null, true);
					tip.setUndecorated(true);
					tip.setSize(210, 40);
					JTextArea t=new JTextArea("你触发了机关!");
					t.setBackground(Color.black);
					t.setForeground(Color.white);
					t.setEditable(false);
					t.setFont(new Font("宋体",Font.BOLD, 30));
					t.addKeyListener(new KeyListener() {
						public void keyTyped(KeyEvent arg0) {}
						public void keyReleased(KeyEvent arg0) {
							switch (arg0.getKeyCode()) {
								case 1000:break;
								default:
									tip.setVisible(false);
							}
						}
						public void keyPressed(KeyEvent arg0) {}
					});
					tip.setLocation(mainframe.getLocation().x+140,mainframe.getLocation().y+180);
					tip.add(t);
					tip.setVisible(true);
					repaint();
				}
				return false;
			}else {
				showMes.setText("击败所有守卫才能打开机关门!");
				return false;
			}
		}else if(map[floor][j][i]==500&&(j!=8||i!=7)) {//下楼
			if(floor>0)
				floor--;
			repaint();
			role.floor=floor;
			showfloor.setText("当前第"+floor+"层");
			return false;
		}
		else if(map[floor][j][i]==500&&(j==8&&i==7)) {//下楼
			if(floor>0)
				floor=10;
			repaint();
			role.floor=floor;
			showfloor.setText("当前第"+floor+"层");
			return false;
		}
		else if(map[floor][j][i]==501&&(j!=13||i!=1)) {//上楼
			floor++;
			repaint();
			role.floor=floor;
			showfloor.setText("当前第"+floor+"层");
			if(floor>maxfloor) {
				maxfloor=floor;
				role.maxfloor=floor;
			}
			return false;
		}
		else if(map[floor][j][i]==501&&floor==10&&j==13&&i==1) {//上楼
			floor=12;
			repaint();
			role.floor=floor;
			showfloor.setText("当前第"+floor+"层");
			if(floor>maxfloor) {
				maxfloor=floor;
				role.maxfloor=floor;
			}
			return false;
		}
		else if(map[floor][j][i]>=800 && map[floor][j][i]<=806) {
			
			meetNpc(map[floor][j][i]);
			if(disp) {
				map[floor][j][i]=0;
				disp=false;
			}
			return false;
		}
    	return true;
    }
	
	private void move(int event) {
    	switch(event) {
    		case LEFT:
    			if(isAllow(x-1, y)) x--;
    			break;
    		case RIGHT:
    			if(isAllow(x+1, y)) x++;	
    			break;
    		case UP:
    			if(isAllow(x, y-1)) y--;
    			break;
    		case DOWN:
    			if(isAllow(x, y+1)) y++;
    			break;	
    		default:
    			break;
    	}
    }
	private void reGame() {//重新开始游戏
		new MyPanel();
		mainframe.dispose();
	}
	
	private void GameOver() {//游戏结束界面
		JDialog gameover=new JDialog(mainframe, null, true);
		JPanel gp=new JPanel(null);
		JLabel gameovertip=new JLabel("你失去了拯救公主的机会");
		JLabel regame=new JLabel("重新再来");
		JLabel quit=new JLabel("退出游戏");
		gameover.setSize(400,180);
		gameover.setLocation(mainframe.getLocation().x+48,mainframe.getLocation().y+130);
		gameovertip.setBounds(30, 10, 360, 110);
		gameovertip.setFont(new Font("宋体",Font.BOLD, 26));
		gameovertip.setBackground(Color.black);
		gameovertip.setForeground(Color.white);
		regame.setBounds(40, 140, 100, 30);
		regame.setFont(new Font("宋体",Font.BOLD, 21));
		regame.setBackground(Color.black);
		regame.setForeground(Color.white);
		regame.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {
				gameover.setVisible(false);
				reGame();
			}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseClicked(MouseEvent arg0) {}
		});
		quit.setBounds(280, 140, 100, 30);
		quit.setFont(new Font("宋体",Font.BOLD, 21));
		quit.setBackground(Color.black);
		quit.setForeground(Color.white);
		quit.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {
				gameover.setVisible(false);
				mainframe.dispose();
			}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseClicked(MouseEvent arg0) {}
		});
		gp.setSize(400,180);
		gp.setBackground(Color.black);
		gp.add(gameovertip);
		gp.add(regame);
		gp.add(quit);
		gameover.add(gp);
		gameover.setUndecorated(true);
		gameover.setVisible(true);	
	}
	public void mouseClicked(MouseEvent arg0) {	
	}
	public void mouseEntered(MouseEvent arg0) {
	}
	public void mouseExited(MouseEvent arg0) {
	}
	public void mousePressed(MouseEvent arg0) {
		if(arg0.getSource()==wp[0]) {//怪物图鉴
			if(goods[0].ex) {//获得此物品后才有功能
				JDialog book=new JDialog(mainframe,"",true);
				book.setUndecorated(true);
				book.setSize(350,450);
				book.setLocation(mainframe.getLocation().x+90,mainframe.getLocation().y+70);
				JPanel bookpanel=new JPanel();
				JLabel booktip=new JLabel("当前楼层怪物的属性");
				JLabel booktip2=new JLabel("(按任意键退出此界面)");
				booktip.setBounds(100, 0, 200, 35);
				booktip2.setBounds(100, 417, 200, 35);
				booktip.setFont(new Font("宋体",Font.BOLD, 15));
				booktip.setFont(new Font("宋体",Font.BOLD, 13));
				booktip.setForeground(Color.WHITE);
				booktip2.setForeground(Color.green);
				bookpanel.setLayout(null);
				bookpanel.setSize(350,450);
				bookpanel.setBackground(Color.black);
				bookpanel.add(booktip);
				book.add(bookpanel);
				bookpanel.add(booktip2);
				book.addKeyListener(new KeyListener() {
					public void keyTyped(KeyEvent arg0) {
					}
					public void keyReleased(KeyEvent arg0) {
						switch(arg0.getKeyCode()){
							case 1111111:
							default:book.setVisible(false);
						}
						repaint();
					}
					public void keyPressed(KeyEvent arg0) {
					}
				});
				int[] record=new int[15*15];
				int num=0;
				for(int h=0;h<15;h++) {
					for(int k=0;k<15;k++) {
						if(num>=1) {
							if((record[num-1]!=map[floor][k][h]) && map[floor][k][h]>=14 && map[floor][k][h]<=55) {
								record[num]=map[floor][k][h];
								num++;
							}else {
								record[num]=0;
								num++;
							}
						}else {
							record[num]=map[floor][k][h];
							num++;
						}
					}
				}
				Arrays.sort(record);
				num=0;
				for(int h=0;h<15*15;h++) {
					if(record[h]!=0)
						num++;
				}
				int[] mon=new int[num];
				num=0;
				for(int h=1;h<15*15;h++) {
					if(record[h]!=0) {
						if(num<=0) {
							mon[num]=record[h];
							num++;
						}else {
							if(record[h]!=0 && mon[num-1]!=record[h]) {
								mon[num]=record[h];
								num++;
							}
						}
					}
				}
				for(int index=1;index<num;index++) {
					JPanel one=new JPanel();
					one.setLayout(null);
					one.setBounds(5, 35+(index-1)*38, 340, 35);
					JLabel mph1=new JLabel();
					JLabel matt1=new JLabel(m[mon[index]].name+" 血量:"+m[mon[index]].hp+" 攻:"+m[mon[index]].atk+" 防:"+m[mon[index]].def+" 金:"+m[mon[index]].meney);
					JLabel mharm1=new JLabel("怪物对你造成的伤害为:"+monsterharm(m[mon[index]]));
					mph1.setIcon(new ImageIcon(getClass().getResource("/res/image/Master/"+(m[mon[index]].ID)+".png")));
					mph1.setBounds(45, 1, 32, 32);
					matt1.setBounds(85, 3, 260, 14);
					mharm1.setBounds(85, 19, 260, 14);
					one.add(mph1);
					one.add(matt1);
					one.add(mharm1);
					bookpanel.add(one);
				}
				book.setVisible(true);
				book.setFocusable(true);
			}
			requestFocus(true);
		}	
		if(arg0.getSource()==wp[1]) {//飞行器
			if(goods[1].ex) {
				JDialog fly=new JDialog(mainframe, null, true);
				JPanel flypanel=new JPanel(null);
				flypanel.setSize(300, 440);
				flypanel.setBackground(Color.BLACK);
				flypanel.addKeyListener(new KeyListener() {
					public void keyTyped(KeyEvent arg0) {}
					public void keyReleased(KeyEvent arg0) {
						if(arg0.getKeyCode()==KeyEvent.VK_SPACE) {
							fileD.setVisible(false);
							repaint();
						}
					}
					public void keyPressed(KeyEvent arg0) {}
				});
				fly.setSize(300, 440);
				fly.setUndecorated(true);
				fly.setLocation(mainframe.getLocation().x+100, mainframe.getLocation().y+65);
				fly.add(flypanel);
				JLabel flytip=new JLabel("当前:第"+floor+"层,请选择飞跃到:");
				flytip.setBounds(70	, 4,180,25);
				flytip.setForeground(Color.WHITE);
				flypanel.add(flytip);
				for(int g=1;g<=maxfloor;g++) {
					JButton fb=new JButton("第"+g+"层");
					fb.setName(""+g);
					fb.setBorder(BorderFactory.createEtchedBorder());
					fb.setBackground(Color.WHITE);
					fb.setBounds(15+55*((g-1)%5), 35+40*((g-1)/5), 50, 30);
					fb.addKeyListener(new KeyListener() {
						public void keyTyped(KeyEvent arg0) {}
						public void keyReleased(KeyEvent arg0) {
							if(arg0.getKeyCode()==KeyEvent.VK_SPACE) {
								role.x=7;
								role.y=1;
								x=7;y=1;
								repaint();
							}
						}
						public void keyPressed(KeyEvent arg0) {}
					});
					fb.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							fly.setVisible(false);
							floor=Integer.parseInt(((JButton)(arg0.getSource())).getName());
							role.floor=floor;
							showfloor.setText("当前第"+floor+"层");
							for(int h=1;h<14;h++) {
								for(int k=1;k<14;k++) {
									if(floor!=1 && map[floor][k][h]==500) {
										role.x=h;
										role.y=k;
										x=h;y=k;
										if(floor==6) {
											role.x=2;
											role.y=1;
											x=2;y=1;
										}
									}else if(floor==1 && map[floor][k][h]==500){
										role.x=7;
										role.y=13;
										x=7;y=13;
									}
								}
							}
							repaint();
							System.out.println("我在"+floor+"楼");
						}
					});
					flypanel.add(fb);
				}
				
				fly.setVisible(true);
			}
			requestFocus(true);
		}
		if(arg0.getSource()==wp[2]) {//稿
			if(goods[2].ex) {
				if (goods[2].ex) {
					if (role.y - 1 > 0 && map[floor][role.y - 1][role.x] == 1) {
						map[floor][role.y - 1][role.x] = 0;
					}
					if (role.x + 1 < 14 && map[floor][role.y][role.x + 1] == 1) {
						map[floor][role.y][role.x + 1] = 0;
					}
					if (role.y + 1 < 14 && map[floor][role.y + 1][role.x] == 1) {
						map[floor][role.y + 1][role.x] = 0;
					}
					if (role.x - 1 > 0 && map[floor][role.y][role.x - 1] == 1) {
						map[floor][role.y][role.x - 1] = 0;
					}
					goods[2].ex = false;
					wp[2].setText("X");
					repaint();
				}
			}
			requestFocus(true);
		}
		
		//存档按钮
		if(arg0.getSource()==save) {
			isSave=true;
			showFileofGame(isSave);
		}
		//读档按钮
		if (arg0.getSource() == load) {
			isSave = false;
			showFileofGame(isSave);
		}
		//存读档面板
		if (arg0.getSource() == filep) {
			fileD.setVisible(false);
		}
		//存读档按钮1
		if (arg0.getSource() == file1) {
			try {
				if (!isSave) {
					JDialog su = new JDialog(fileD, null, true);
					su.setSize(250, 80);
					su.setUndecorated(true);
					su.setLocation(fileD.getLocation().x + 25, fileD.getLocation().y + 80);
					su.setLayout(null);
					JLabel tip = new JLabel("确定读取存档吗?");
					tip.setFont(new Font("默认", 1, 24));
					tip.setBounds(15, 10, 220, 30);

					JLabel tip1 = new JLabel("确定");
					tip1.setFont(new Font("默认", 1, 19));
					tip1.setBounds(45, 50, 42, 20);
					tip1.setBorder(BorderFactory.createLineBorder(Color.black));
					tip1.addMouseListener(new MouseListener() {
						public void mouseReleased(MouseEvent arg0) {
						}

						public void mousePressed(MouseEvent arg0) {
							try {
								GameFile(file1, isSave);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						public void mouseExited(MouseEvent arg0) {
						}

						public void mouseEntered(MouseEvent arg0) {
						}

						public void mouseClicked(MouseEvent arg0) {
						}
					});

					JLabel tip2 = new JLabel("算了");
					tip2.setFont(new Font("默认", 1, 19));
					tip2.setBounds(155, 50, 42, 20);
					tip2.setBorder(BorderFactory.createLineBorder(Color.black));
					tip2.addMouseListener(new MouseListener() {
						public void mouseReleased(MouseEvent arg0) {
						}

						public void mousePressed(MouseEvent arg0) {
							su.setVisible(false);
						}

						public void mouseExited(MouseEvent arg0) {
						}

						public void mouseEntered(MouseEvent arg0) {
						}

						public void mouseClicked(MouseEvent arg0) {
						}
					});

					su.add(tip);
					su.add(tip1);
					su.add(tip2);
					su.setVisible(true);
				} else {
					GameFile(file1, isSave);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//存读档按钮2
		if (arg0.getSource() == file2) {
			try {
				if (!isSave) {
					JDialog su = new JDialog(fileD, null, true);
					su.setSize(250, 80);
					su.setUndecorated(true);
					su.setLocation(fileD.getLocation().x + 25, fileD.getLocation().y + 80);
					su.setLayout(null);
					JLabel tip = new JLabel("确定读取存档吗?");
					tip.setFont(new Font("默认", 1, 24));
					tip.setBounds(15, 10, 220, 30);

					JLabel tip1 = new JLabel("确定");
					tip1.setFont(new Font("默认", 1, 19));
					tip1.setBounds(45, 50, 42, 20);
					tip1.setBorder(BorderFactory.createLineBorder(Color.black));
					tip1.addMouseListener(new MouseListener() {
						public void mouseReleased(MouseEvent arg0) {
						}

						public void mousePressed(MouseEvent arg0) {
							try {
								GameFile(file2, isSave);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						public void mouseExited(MouseEvent arg0) {
						}

						public void mouseEntered(MouseEvent arg0) {
						}

						public void mouseClicked(MouseEvent arg0) {
						}
					});

					JLabel tip2 = new JLabel("算了");
					tip2.setFont(new Font("默认", 1, 19));
					tip2.setBounds(155, 50, 42, 20);
					tip2.setBorder(BorderFactory.createLineBorder(Color.black));
					tip2.addMouseListener(new MouseListener() {
						public void mouseReleased(MouseEvent arg0) {
						}

						public void mousePressed(MouseEvent arg0) {
							su.setVisible(false);
						}

						public void mouseExited(MouseEvent arg0) {
						}

						public void mouseEntered(MouseEvent arg0) {
						}

						public void mouseClicked(MouseEvent arg0) {
						}
					});

					su.add(tip);
					su.add(tip1);
					su.add(tip2);
					su.setVisible(true);
				} else {
					GameFile(file2, isSave);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//存读档按钮3
		if (arg0.getSource() == file3) {
			try {
				if (!isSave) {
					JDialog su = new JDialog(fileD, null, true);
					su.setSize(250, 80);
					su.setUndecorated(true);
					su.setLocation(fileD.getLocation().x + 25, fileD.getLocation().y + 80);
					su.setLayout(null);
					JLabel tip = new JLabel("确定读取存档吗?");
					tip.setFont(new Font("默认", 1, 24));
					tip.setBounds(15, 10, 220, 30);

					JLabel tip1 = new JLabel("确定");
					tip1.setFont(new Font("默认", 1, 19));
					tip1.setBounds(45, 50, 42, 20);
					tip1.setBorder(BorderFactory.createLineBorder(Color.black));
					tip1.addMouseListener(new MouseListener() {
						public void mouseReleased(MouseEvent arg0) {
						}

						public void mousePressed(MouseEvent arg0) {
							try {
								WebGameFile(file3, isSave);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						public void mouseExited(MouseEvent arg0) {
						}

						public void mouseEntered(MouseEvent arg0) {
						}

						public void mouseClicked(MouseEvent arg0) {
						}
					});

					JLabel tip2 = new JLabel("算了");
					tip2.setFont(new Font("默认", 1, 19));
					tip2.setBounds(155, 50, 42, 20);
					tip2.setBorder(BorderFactory.createLineBorder(Color.black));
					tip2.addMouseListener(new MouseListener() {
						public void mouseReleased(MouseEvent arg0) {
						}

						public void mousePressed(MouseEvent arg0) {
							su.setVisible(false);
						}

						public void mouseExited(MouseEvent arg0) {
						}

						public void mouseEntered(MouseEvent arg0) {
						}

						public void mouseClicked(MouseEvent arg0) {
						}
					});

					su.add(tip);
					su.add(tip1);
					su.add(tip2);
					su.setVisible(true);
				} else {
					WebGameFile(file3, isSave);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void mouseReleased(MouseEvent arg0) {
	}  
}

