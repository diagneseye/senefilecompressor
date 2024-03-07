import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SenFileCompressor {
    private static boolean repertoireObligatoire = true;
    private static boolean verbose = false;
    static ArrayList<String> fichiers = new ArrayList<String>();
    static String cheminVersRepertoire = "";

    public static void main(String[] args) {

        int tailleTableau = args.length;
        int indexDernierFichierSouce;

        if (tailleTableau != 0) {

            switch (args[0]) {

                case "-h":
                    afficherAide();
                    System.exit(0);

                case "-c":
                    indexDernierFichierSouce = collecterFichiersSource(args);
                    cheminVersRepertoire = validerDestinationEtVerbose(args, indexDernierFichierSouce, tailleTableau);
                    Compressor.achivageEtCompression(fichiers, cheminVersRepertoire, repertoireObligatoire,
                            verbose);
                    break;

                case "-d":
                    indexDernierFichierSouce = collecterFichiersSource(args);
                    if (indexDernierFichierSouce != 2) {
                        afficherAide();
                        System.exit(0);
                    }
                    cheminVersRepertoire = validerDestinationEtVerbose(args, indexDernierFichierSouce, tailleTableau);
                    Decompressor.desachivageEtDecompression(fichiers.get(0), cheminVersRepertoire,
                            repertoireObligatoire,
                            verbose);
                    break;
                default:
                    afficherAide();
                    System.exit(0);

            }

        }

    }

    private static int collecterFichiersSource(String[] args) {
        int tailleTableau = args.length;
        int index = 1;
        boolean continuer = true;
        if (index > tailleTableau) {
            afficherAide();
            System.exit(0);
        }

        switch (args[index]) {
            case "-r", "-c", "-h", "-f", "-v":
                afficherAide();
                System.exit(0);

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
                            System.exit(0);

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

    private static String validerDestinationEtVerbose(String[] args, int index, int tailleTableau) {
        String cheminVersRepertoire = "";

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
                                    repertoireObligatoire = false;
                                    index++;
                                    if (index < tailleTableau) {
                                        switch (args[index]) {
                                            case "-v":
                                                verbose = true;
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                    break;
                                case "-v":
                                    verbose = true;
                                    break;

                                default:
                                    break;
                            }
                        }
                    } else {
                        afficherAide();
                        System.exit(0);
                    }
                    break;

                case "-v":
                    verbose = true;
                    break;

                default:
                    afficherAide();
                    System.exit(0);

            }

        } else {
            afficherAide();
            System.exit(0);
        }
        return cheminVersRepertoire;

    }

    private static void afficherAide() {        

        File fileAide = new File("help.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(fileAide))) {
            String ligne =  br.readLine();
            System.out.println(ligne);

            while ((ligne = br.readLine()) != null) {
                System.out.println(ligne.indent(15));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}