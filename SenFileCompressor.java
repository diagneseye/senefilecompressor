import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SenFileCompressor {
    private static boolean creerRepertoire = true;
    private static boolean executionVerbeuse = false;
    static ArrayList<String> fichiers = new ArrayList<String>();
    static String cheminVersRepertoire = "";

    public static void main(String[] args) {

        int tailleTableau = args.length;
        int indexDernierFichierSource;

        if (tailleTableau != 0) {

            switch (args[0]) {

                case "-h":
                    afficherAide();
                case "-c":
                    indexDernierFichierSource = collecterFichiersSource(args);
                    cheminVersRepertoire = destinationEtTypeExecution(args, indexDernierFichierSource, tailleTableau);
                    Compressor compressor = new Compressor(fichiers, cheminVersRepertoire, creerRepertoire,
                    executionVerbeuse);
                    compressor.achivageEtCompression();
                    break;

                case "-d":
                    indexDernierFichierSource = collecterFichiersSource(args);
                    if (indexDernierFichierSource != 2) {
                        afficherAide();
                    }
                    cheminVersRepertoire = destinationEtTypeExecution(args, indexDernierFichierSource, tailleTableau);
                    Decompressor decompressor = new Decompressor(cheminVersRepertoire, creerRepertoire, executionVerbeuse); 
                    decompressor.desachivageEtDecompression(fichiers.get(0), cheminVersRepertoire,
                            creerRepertoire,
                            executionVerbeuse);
                    break;
                default:
                    afficherAide();
            }

        }else{
            afficherAide();
        }

    }

    private static int collecterFichiersSource(String[] args) {
        int tailleTableau = args.length;
        int index = 1;
        boolean continuer = true;
        if (index == tailleTableau) {
            afficherAide();
        }

        switch (args[index]) {

            case "-r", "-c", "-h", "-f", "-v":
                afficherAide();

            default:

                fichiers.add(args[index]);
                index++;

                while (index < tailleTableau && continuer) {

                    switch (args[index]) {

                        case "-r", "-v":
                            continuer = false;
                            break;

                        case "-c", "-h", "-f":
                            afficherAide();

                        default:
                            fichiers.add(args[index]);
                            index++;
                            break;
                    }
                }
                break;
        }
        return index;

    }

    private static String destinationEtTypeExecution(String[] args, int index, int tailleTableau) {

        String cheminVersRepertoire = ".";

        if (index < tailleTableau) {

            switch (args[index]) {
                case "-r":
                    index++;
                    if (index < tailleTableau) {
                        cheminVersRepertoire = args[index];
                        index++;
                        if (index < tailleTableau) {
                            switch (args[index]) {
                                case "-f":
                                    creerRepertoire = false;
                                    index++;
                                    if (index < tailleTableau) {
                                        switch (args[index]) {
                                            case "-v":
                                                executionVerbeuse = true;
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                    break;
                                case "-v":
                                    executionVerbeuse = true;
                                    break;

                                default:
                                    break;
                            }
                        }
                    } else {
                        afficherAide();
                    }
                    break;

                case "-v":
                    executionVerbeuse = true;
                    break;

                default:
                    afficherAide();
            }

        }
        return cheminVersRepertoire;

    }

    private static void afficherAide() {

        File fileAide = new File("help.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(fileAide))) {
            String ligne = br.readLine();
            System.out.println(ligne);

            while ((ligne = br.readLine()) != null) {
                System.out.println(ligne.indent(5));
            }

            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}