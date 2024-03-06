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
                    Decompressor.desachivageEtDecompression(fichiers.get(0),cheminVersRepertoire, repertoireObligatoire,
                    verbose );
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
        // TODO Auto-generated method stub

        String cmd1 = "java SenFileCompressor -c fic1 fic2 fic3 ... [-r cheminVersRepertoire [-f] ] [-v]";
        String cmd1_usage = "(Pour archiver et compresser des fichiers)";
        String cmd2 = "ou java SenFileCompressor -d fichier.sfc [-r cheminVersRepertoire [-f] ] [-v]";
        String cmd2_usage = "(Pour décompresser  et désarchiver un fichier)";
        String cmd3 = "ou java SenFileCompressor -h ";
        String cmd3_usage = "(Pour afficher l'aide)";

        String[] details = { "Détails des options : -c <liste fichier à compresser> 2" + //
                "devra fournir en sortie un \r\n" + //
                "fichier d'extension « .sfc » qui regroupe, sous forme compressé, les différents fichiers \r\n" + //
                "fournis en paramètre ",
                "java SenFileCompressor -d fichierADecompresser.sfc devra fournir en sortie \r\n" + //
                        "l'intégralité des fichiers contenus dans l'archive donné en paramètre" };

        System.out.println("Usage : " + cmd1);
        System.out.println(cmd1_usage.indent(15));
        System.out.println(cmd2.indent(5));
        System.out.println(cmd2_usage.indent(15));
        System.out.println(cmd3.indent(5));
        System.out.println(cmd3_usage.indent(15));
        for (String detail : details) {

            System.out.println(detail.indent(5));
        }
    }

}