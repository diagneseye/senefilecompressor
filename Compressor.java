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
    
    public static void achivageEtCompression(ArrayList<String> fichiers, String cheminVersRepertoire,
            boolean repertoireObligatoire, boolean verbose) {
    

        if (verbose) {
            System.out.println("L'éxécution se fera de manière verbeuse");
        }
        archivage(fichiers, cheminVersRepertoire, repertoireObligatoire);
        compression(cheminVersRepertoire);
    }

    public static void archivage(ArrayList<String> fichiers, String cheminVersRepertoire ,boolean repertoireObligatoire) {
        ArrayList<File> files = new ArrayList<File>();
        FileOutputStream fos;
        DataOutputStream dos;
        FileInputStream fis;
        byte[] tb = new byte[1024];
       
        File  repertoireDestination = new File(cheminVersRepertoire);

        for (String fichier : fichiers) {
            File newfile = new File(fichier);
            if (newfile.exists()) {
                files.add(newfile);
            }else{
                System.out.printf("Le fichier %s est introuvable \n", fichier);
                System.exit(0);
            }
        }
        
        if (!repertoireDestination.isDirectory()) {
            if (!repertoireObligatoire) {
                repertoireDestination.mkdir();
            }else{
                // throw new ExceptionRepertoireIntrouvable extends Exception("Dossier introuva");
                System.out.println( "Répertoire (obligatoire) introuvable");
                System.exit(0);
            }
        }
        File  fichierArchive = new File(repertoireDestination.getPath()+File.separator+"fichier.tmp");

        try {
            fos = new FileOutputStream(fichierArchive);
            dos = new DataOutputStream(fos);
            // On recupére le nombre de fichiers, qu'on enregistre en premier sur notre
            // fichier archive
            dos.writeInt(fichiers.size());
            // Pour chaque fichier dans notre liste de fichier
            for (File file : files) {
                // On crée un objet de type FileInputStream
                fis = new FileInputStream(file);
                // On recupére le nom du fichier pour l'enregistré dans le fichier archive
                dos.writeUTF(file.getName());
                // On recupére la taille du fichier pour l'enregistré dans le fichier archive
                dos.writeLong(file.length());
                int nbLu;
                while ((nbLu = fis.read(tb)) != -1) {

                    dos.write(tb);
                }

                fis.close();
            }

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

    public static void compression(String cheminVersRepertoire) {
        FileInputStream fis;
        DataInputStream dis;
        FileOutputStream fos;
        DataOutputStream dos;
        Deflater deflater = new Deflater();
        byte[] inTb = new byte[1024];
        byte[] outTb = new byte[1024];
        int nbBytes = 0;
        File  fichierArchive = new File(cheminVersRepertoire+File.separator+"fichier.tmp");
        File  fichierCompresse = new File(cheminVersRepertoire+File.separator+"fichier.sfc");

        try {
            fis = new FileInputStream(fichierArchive);
            dis = new DataInputStream(fis);
            fos = new FileOutputStream(fichierCompresse);
            dos = new DataOutputStream(fos);
            int nbLu;

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
}


// class ExceptionRepertoireIntrouvable extends Exception {
//     ExceptionRepertoireIntrouvable(String msg){ super(msg);}
    
// }