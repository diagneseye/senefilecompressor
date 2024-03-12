import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.Deflater;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;

// Class Compressor (Cheikh A Diagne SEYE - Ismaila DIATTA)
public class Compressor {

    private final String NOM_FICHIER_TEMP = "fichier.tmp";
    private final String NOM_FICHIER_COMPRESSE = "fichier.sfc";

    private String cheminVersRepertoire;
    private ArrayList<String> fichiers;
    private boolean creerRepertoire;
    private boolean executionVerbeuse;

    public Compressor(ArrayList<String> fichiers, String cheminVersRepertoire, boolean creerRepertoire,
            boolean executionVerbeuse) {
        this.fichiers = fichiers;
        this.cheminVersRepertoire = cheminVersRepertoire;
        this.creerRepertoire = creerRepertoire;
        this.executionVerbeuse = executionVerbeuse;
    }

    public void achivageEtCompression() {
        File repertoireDestination = new File(cheminVersRepertoire);
        archivage(repertoireDestination);
        compression();
    }

    public void archivage(File repertoireDestination) {
        ArrayList<File> files = new ArrayList<File>();
        FileOutputStream fos;
        DataOutputStream dos;
        FileInputStream fis;
        byte[] tb = new byte[1024];

        afficherDetailsExecution("Vérification des fichiers");
        for (String fichier : fichiers) {
            File newfile = new File(fichier);
            if (newfile.exists()) {

                files.add(newfile);
            } else {
                System.out.printf("Le fichier %s est introuvable \n", fichier);
                System.exit(0);
            }
        }
        
        afficherDetailsExecution("Vérification du répertoire de destination");
        if (!repertoireDestination.isDirectory()) {

            if (!creerRepertoire) {
                afficherDetailsExecution("Création répertoire de destination");
                repertoireDestination.mkdir();
            } else {
                System.out.println("Répertoire (obligatoire) introuvable");
                System.exit(0);
            }
        }

        File fichierArchive = new File(repertoireDestination.getPath() + File.separator + NOM_FICHIER_TEMP);

        try {
            fos = new FileOutputStream(fichierArchive);
            dos = new DataOutputStream(fos);
            dos.writeInt(fichiers.size());

            afficherDetailsExecution("Début archivage");

            for (File file : files) {
                fis = new FileInputStream(file);
                dos.writeUTF(file.getName());
                dos.writeLong(file.length());
                int nbLu;
                while ((nbLu = fis.read(tb)) != -1) {

                    dos.write(tb);
                }

                fis.close();
            }
            afficherDetailsExecution("Fin archivage");
            fos.close();
            dos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void compression() {
        FileInputStream fis;
        DataInputStream dis;
        FileOutputStream fos;
        DataOutputStream dos;
        Deflater deflater = new Deflater();
        byte[] inTb = new byte[1024];
        byte[] outTb = new byte[1024];
        int nbBytes = 0;
        File fichierArchive = new File(cheminVersRepertoire + File.separator + NOM_FICHIER_TEMP);
        File fichierCompresse = new File(cheminVersRepertoire + File.separator + NOM_FICHIER_COMPRESSE);

        try {
            fis = new FileInputStream(fichierArchive);
            dis = new DataInputStream(fis);
            fos = new FileOutputStream(fichierCompresse);
            dos = new DataOutputStream(fos);
            int nbLu;

            afficherDetailsExecution("Début Compression");

            while (true) {
                nbLu = fis.read(inTb);
                if (nbLu == -1) {
                    deflater.finish();
                    while (!deflater.finished()) {
                        nbBytes = deflater.deflate(outTb, 0, outTb.length);
                        if (nbBytes > 0) {
                            fos.write(outTb, 0, nbBytes);
                        }
                    }
                    break;
                } else {
                    deflater.setInput(inTb, 0, nbLu);
                    while (!deflater.needsInput()) {
                        nbBytes = deflater.deflate(outTb, 0, outTb.length);
                        if (nbBytes > 0) {
                            fos.write(outTb, 0, nbBytes);
                        }
                    }
                }
            }

            afficherDetailsExecution("Fin Compression");

            fis.close();
            fos.close();
            dos.close();
            dis.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
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

    public String getCheminVersRepertoire() {
        return cheminVersRepertoire;
    }

    public void setFichiers(ArrayList<String> fichiers) {
        this.fichiers = fichiers;
    }

    public ArrayList<String> getFichiers() {
        return fichiers;
    }

    public void setCreerRepertoire(boolean creerRepertoire) {
        this.creerRepertoire = creerRepertoire;
    }

    public Boolean getCreerRepertoire() {
        return creerRepertoire;
    }

    public void setExecutionVerbeuse(boolean executionVerbeuse) {
        this.executionVerbeuse = executionVerbeuse;
    }

    public boolean getExecutionVerbeuse() {
        return executionVerbeuse;
    }

}
