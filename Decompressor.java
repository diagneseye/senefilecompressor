import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class Decompressor {

    public Decompressor() {
    }

    public static void desachivageEtDecompression(String fichier, String cheminVersRepertoire,
            boolean repertoireObligatoire,
            boolean verbose) {

        if (verbose) {
            System.out.println("L'éxécution se fera de manière verbeuse");
        }

        File fichierCompresse = new File(fichier);
        File fichierArchive = new File("fichierdesar.tmp");
        File repertoireDestination = new File(cheminVersRepertoire);

        if (fichierCompresse.exists()) {

            decompression(fichierCompresse, fichierArchive);

            if (!repertoireDestination.isDirectory()) {
                if (!repertoireObligatoire) {
                    repertoireDestination.mkdir();
                } else {
                    System.out.println("Répertoire (obligatoire) introuvable");
                    System.exit(0);
                }
            }
            
           
            desarchivage("fichierdesar.tmp", repertoireDestination.getPath());

        } else {
            System.out.println("Fichier source n'existe pas");
            System.exit(0);
        }
    }

    public static void desarchivage(String fichier, String cheminVersRepertoire) {
        FileInputStream fis;
        DataInputStream dis;
        FileOutputStream fos;
        File file;
        byte[] tb;

        file = new File(fichier);
        try {
            fis = new FileInputStream(file);
            dis = new DataInputStream(fis);
            int nbFichier = dis.readInt();
            for (int i = 1; i <= nbFichier; i++) {
                String nomFichier = dis.readUTF();
                fos = new FileOutputStream(cheminVersRepertoire + File.separator + nomFichier);
                Long tailleFichier = dis.readLong();
                tb = new byte[1024];
                
                while (tailleFichier > 0) {
                    tailleFichier -= dis.read(tb);
                    fos.write(tb);
                }
                fos.flush();
                fos.close();
            }
            fis.close();
            dis.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void decompression(File fichierCompresse, File fichierArchive) {
        FileInputStream fis;
        FileOutputStream fos;
        DataOutputStream dos;
        Inflater inflater = new Inflater();
        byte[] inTb = new byte[1024];
        byte[] outTb = new byte[1024];
        int nbBytes = 0;

        try {
            fis = new FileInputStream(fichierCompresse);
            InflaterInputStream iin = new InflaterInputStream(fis);
            fos = new FileOutputStream(fichierArchive);
            dos = new DataOutputStream(fos);
            int nbLu;

            while ((nbLu = iin.read(inTb)) != -1) {

                dos.write(inTb, 0, nbLu);

            }

            iin.close();
            fis.close();
            fos.close();
            dos.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}