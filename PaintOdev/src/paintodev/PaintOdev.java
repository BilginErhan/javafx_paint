
package paintodev;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;         //gerekli kütüphaneler eklenmiştir.
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author ERHAN
 */
public class PaintOdev extends JFrame{// PaintOdev classı JFrame den türetilmiştir.
    
    JButton fircaBut, cizgiBut, elipsBut, dikBut;       //Butonalar tanımlandı

    Graphics2D grafikAyarlari;      //Grafik ayarları için değişken tanımlaması
    int hareket=1;          //hangi butona tıklandığını tutan değişken


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        PaintOdev paintOdev = new PaintOdev();      // yapıcı fonksiyon çağırılır
    }
    
    public PaintOdev()
    {
        setSize(700,700);       //çerçevinin boyutu tanımlanır
        setTitle("Paint"); //çerçevenin başlığı tanımlandı
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //uygulamadan çıkılınca tüm programı sonladırma
        getContentPane().setBackground(Color.WHITE);    //JComponent arka planı beyaz yapımıştır.
        JPanel butonPanel = new JPanel();   //butonları koyacağımız JPanel nesnesi tanımlanmıştır
        Box kutu = Box.createVerticalBox(); //Kutu nesnesi tanımlanarak kutuları dikey bir biçimde yerleştirme tanımlandı
       
       fircaBut = beniButonYap("firca",1);
       cizgiBut = beniButonYap("cizgi",2);  //beniButonYap fonsksiyonuna yollaanrak butonlar tanımlanmıştır.
       elipsBut = beniButonYap("elips",3);
       dikBut = beniButonYap("dik",4);
       
    
       kutu.add(fircaBut);
       kutu.add(cizgiBut);  //hepsi kutu nesnesine eklenmiştir.
       kutu.add(elipsBut);
       kutu.add(dikBut);
    

       
       butonPanel.add(kutu); //Jpanel nesnesine kutu nesnemizi ekledik
       
       this.add(butonPanel,BorderLayout.WEST); //butonlar sola dayalı dikey şekilde sıralandı
       this.add(new cizim(),BorderLayout.CENTER);   //cizim yapacağımız cizim classı sayfanın ortasına yerleştirildi.
       this.setVisible(true);   //Program çalıştırıldığında JFrame i görünür yapar.
       
    }
    
    public JButton beniButonYap(String yazi,final int hareketSayisi)
    {   //beniButonYap fonskyinoundaalınan değerlere göre buton yapılır ve değişken değeri tanımlanır
        JButton but = new JButton();    
        but.setText(yazi);  //üzerinde yazan yazı tanımlanır
        but.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e){
                hareket = hareketSayisi;//hareket değişkeninde buton numarası tutulur
            }
        
        });
        return but;//değergeri döndürülür.
    }

    
    private class cizim extends JComponent{
        //cizim nesnesmiz JComponent den türetilmiştir
        ArrayList<Shape> sekiller = new ArrayList<Shape>(); //çizimlerin yapılıp tutulacağı dinamik ArrayList     
        Point cizimBaslangic,cizimSon;  //mouse un x ve y koordinatını tutan değişkenler
        
        public cizim()
        {
            this.addMouseListener(new MouseAdapter(){
                //mouse olayları dinlenilir
                public void mousePressed(MouseEvent e)
                {//mouse a basılı tutulduğunda bu fonksiyonagirilir
                    if (hareket != 1)
                    {//eğer buton 1 e tıklı değilse 
                        cizimBaslangic = new Point(e.getX(),e.getY()); //ilk tıklandığı koordinatlar alınır
                        cizimSon = cizimBaslangic;
                        repaint();
                    }
                }
                public void mouseReleased(MouseEvent e)
                {//mouse serbest bırakıldığında 
                    if(hareket != 1)
                    {//buton 1 yani fırça ya tıklanıldıysa birşey yapmaz
                        Shape sekil = null;
                    if(hareket == 2)
                    {//ama diğer butonlara tıklandıysa gerekli çizim fonskyinona mouseun ilk ve son tıklandığı konum
                        //yollanır
                        sekil = cizgiCiz(cizimBaslangic.x,cizimBaslangic.y,e.getX(),e.getY());
                    }
                    else if(hareket == 3)
                    {
                        sekil = elipsCiz(cizimBaslangic.x,cizimBaslangic.y,e.getX(),e.getY());
                    }
                    else if(hareket == 4)
                    {
                        sekil = dikdortgenCiz(cizimBaslangic.x,cizimBaslangic.y,e.getX(),e.getY());
                    }
    
                    sekiller.add(sekil);//fonskiyondan gelen shape sekili sekiller ArrayListine eklenir
                    
                    cizimBaslangic = null;
                    cizimSon = null;//mouse koordinatları sıfırlanır
                    repaint();
                    }
                }
            });
            
            this.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if(hareket ==1)
                    {//eğer mouse bırakılır ve buton fırçaya tıklı ise
                        int x=e.getX();//mouse koordinatları alınır ve 
                        int y=e.getY();

                        Shape sekil=null;
                        sekil=fircaCiz(x,y,5,5);//çizim için yollanılır
                        sekiller.add(sekil);//array liste eklenir

                    }
                    cizimSon = new Point(e.getX(),e.getY());//cizimSon değişkenine mouse bırakıldığı koordinatlar girilir
                    repaint();
                    
                }

                @Override
                public void mouseMoved(MouseEvent e) {//mouse haraket edince herhangi bir işlem yapılmaz bu fonksiyonda
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
        }
        public void paint(Graphics g)
        {
            grafikAyarlari = (Graphics2D) g;
            grafikAyarlari.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            grafikAyarlari.setStroke(new BasicStroke(7));
            
            
            
            for (Shape s: sekiller)
            {
                grafikAyarlari.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                grafikAyarlari.setPaint(Color.black);//Şekil kenarlık rengi siyah olarak belirlendi
                grafikAyarlari.draw(s);//şekil çizdilirilde
                grafikAyarlari.setPaint(Color.BLACK);//şekil dolgu rengi siyah olarak belirlendi
                grafikAyarlari.fill(s);//içi siyah renk ile dolduruldu
            }
            
            if(cizimBaslangic != null && cizimSon != null)
            {//eğer kullanıcı çizim yaparken bir transparan görüntü oluşturararak nasıl bir çizim yaptığını gösteren fonksiyon
                grafikAyarlari.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.40f));
                //setComposite de 0.40f lik transparan değeri verilir ve çizim transparanlaştırılır
                grafikAyarlari.setPaint(Color.LIGHT_GRAY);
                
                Shape sekil= null;
                
                if(hareket == 2)
                {
                    sekil = cizgiCiz(cizimBaslangic.x,cizimBaslangic.y,cizimSon.x,cizimSon.y);
                }
                else if(hareket == 3)
                {
                    sekil = elipsCiz(cizimBaslangic.x,cizimBaslangic.y,cizimSon.x,cizimSon.y);
                }
                else if(hareket == 4)
                {
                    sekil = dikdortgenCiz(cizimBaslangic.x,cizimBaslangic.y,cizimSon.x,cizimSon.y);
                }
                grafikAyarlari.draw(sekil);
            }  
        }
   
        private Rectangle2D.Float dikdortgenCiz(int x1,int y1,int x2, int y2)
        {   //diktörtgen çizim fonksiyonu
            int x = Math.min(x1,x2);    //başangıç koordinatı belirlenir
            int y = Math.min(y1,y2);    //bitiş koordinatı
            int genislik = Math.abs(x1-x2); //genişlik değerii
            int yukseklik = Math.abs(y1-y2); //yükseklik değeri hesaplanır

            return new Rectangle2D.Float(x,y,genislik,yukseklik);   //geriye Shape tipinde değişken yollanır
        }
        private Ellipse2D.Float elipsCiz(int x1,int y1, int x2, int y2)
        {
            int x = Math.min(x1,x2);    
            int y = Math.min(y1,y2);
            int genislik = Math.abs(x1-x2);
            int yukseklik = Math.abs(y1-y2);

            return new Ellipse2D.Float(x,y,genislik,yukseklik);
        }
        private Line2D.Float cizgiCiz(int x1,int y1,int x2, int y2)
        {
            return new Line2D.Float(x1,y1,x2,y2);
        }
        private Ellipse2D.Float fircaCiz(int x1,int y1, int fircaKenarlikGenisligi, int fircaKenarlikYuksekligi)
        {
            return new Ellipse2D.Float(x1,y1,fircaKenarlikGenisligi,fircaKenarlikYuksekligi);
        }
    }
}