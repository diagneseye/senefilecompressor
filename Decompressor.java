import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class Decompressor {

    private final String NOM_FICHIER_ARCHIVE = "fichier.sfc";
    private final String NOM_FICHIER_DESAR_TEMP = "fichierdesar.tmp";

    private String cheminVersRepertoire;
    private boolean creerRepertoire;
    private boolean executionVerbeuse;

    public Decompressor(String cheminVersRepertoire, boolean creerRepertoire, boolean executionVerbeuse) {
        this.cheminVersRepertoire = cheminVersRepertoire;
        this.creerRepertoire = creerRepertoire;
        this.executionVerbeuse = executionVerbeuse;
    }

    public void desachivageEtDecompression(String fichier, String cheminVersRepertoire,
            boolean repertoireObligatoire,
            boolean verbose) {

        File fichierCompresse = new File(fichier);
        File repertoireDestination = new File(cheminVersRepertoire);
        
        afficherDetailsExecution("Vérification fichier source");
        if (fichierCompresse.exists()) {
            
            afficherDetailsExecution("Vérification du répertoire de destination");
            
            if (!repertoireDestination.isDirectory()) {
                if (!repertoireObligatoire) {
                    afficherDetailsExecution("Création du répertoire de destination");
                    repertoireDestination.mkdir();
                } else {
                    System.out.println("Répertoire (obligatoire) introuvable");
                    System.exit(0);
                }
            }
            afficherDetailsExecution("Création fichierdesarchive temporaire");
            File fichierArchive = new File(cheminVersRepertoire + File.separator + NOM_FICHIER_DESAR_TEMP);
            
            decompression(fichierCompresse, fichierArchive);
            desarchivage(fichierArchive);

        } else {
            System.out.println("Fichier source n'existe pas");
            System.exit(0);
        }
    }

    public void decompression(File fichierCompresse, File fichierArchive) {
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

            afficherDetailsExecution("Début décompression");
            while ((nbLu = iin.read(inTb)) != -1) {
                dos.write(inTb, 0, nbLu);
            }
            afficherDetailsExecution("Fin décompression");

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

    public void desarchivage(File fichierArchive) {
        FileInputStream fis;
        DataInputStream dis;
        FileOutputStream fos;
        File file;
        byte[] tb;

        try {
            fis = new FileInputStream(fichierArchive);
            dis = new DataInputStream(fis);
            int nbFichier = dis.readInt();
            afficherDetailsExecution("Début désarchivage");
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
            afficherDetailsExecution("Fin désarchivage");
            fis.close();
            dis.close();
            // afficherDetailsExecution("Suppression fichierdesarchive temporaire");
            // fichierArchive.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void afficherDetailsExecution(String message) {
        if (executionVerbeuse) {
            System.out.println(message);
        }
    }

    public void setCheminVersRepertoire(String cheminVersRepertoire) {
        this.cheminVersRepertoire = cheminVersRepertoire;
    }

    public void setCreerRepertoire(boolean creerRepertoire) {
        this.creerRepertoire = creerRepertoire;
    }

    public void setExecutionVerbeuse(boolean executionVerbeuse) {
        this.executionVerbeuse = executionVerbeuse;
    }

    public String getCheminVersRepertoire() {
        return cheminVersRepertoire;
    }

    public boolean isCreerRepertoire() {
        return creerRepertoire;
    }

    public boolean isExecutionVerbeuse() {
        return executionVerbeuse;
    }
}