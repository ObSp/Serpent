package JGamePackage.JGame.Classes.Services;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.JOptionPane;

import JGamePackage.JGame.Classes.StudioClasses.ProjectRepresentation;
import JGamePackage.lib.CustomError.CustomError;

/**This service is used to perform JGameStudio-related operations.
 * 
 */
public class StudioService extends Service {
    private CustomError ErrorNoProject = new CustomError("Unable to %s: the directory %s is not a JGameStudio project.", CustomError.ERROR, "JGamePackage");

    private ProjectRepresentation projectRepresentation;
    private String jgameSourceVersion;

    private void assertProjectStatus(String failMessage) {
        if (!IsStudioProject()) {
            ErrorNoProject.Throw(new String[] {failMessage, new File("").getAbsolutePath().replaceAll("\\\\", "/")}); //need to replace slashes otherwise they dont show up in the error message
        }
    }

    public StudioService(boolean autoUpdate) {
        super();

        if (!IsStudioProject()) return;

        BuildProjectRepresentation();

        game.GetWindow().setTitle(projectRepresentation.projectJson.projectName);

        if (!autoUpdate) return;

        try {
            jgameSourceVersion = Files.readAllLines(Paths.get(projectRepresentation.projectJson.jgamePackageSource + "\\.version")).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!IsJGamePackageUpToDate()) {
            UpdateJGamePackageToLatestVersion();
        }
    }

    private void BuildProjectRepresentation() {
        try {
            projectRepresentation = new ProjectRepresentation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Force-updates the JGame package to the latest version. Note that this method doesn't
     * check if the package is already up to date, so it is recommended to call {@code StudioService.IsJGamePackageUpToDate()} before calling this method.
     * 
     */
    public void UpdateJGamePackageToLatestVersion() {
        assertProjectStatus("update the JGame package to the latest version");

        String ANSI_GREEN = "\u001B[32m";
        String ANSI_RESET = "\u001B[0m";

        File packageDir = new File("JGamePackage");

        deleteDir(packageDir);
        packageDir.mkdir();

        copyDir(projectRepresentation.projectJson.jgamePackageSource, "JGamePackage", true);

        System.out.println(ANSI_GREEN +  
        "Your JGame package has been updated updated to the latest version(v" + jgameSourceVersion + "). Please restart your JGame program. Thanks for using JGame!"
        + ANSI_RESET);

        JOptionPane.showMessageDialog(null, 
            "Your JGame package has been updated updated to the latest version(v" + jgameSourceVersion + "). Please restart your JGame program. Thanks for using JGame! \n\nIf you'd like to stop receiving automatic updates, please set StartParams.autoUpdate to false when initializing JGame.", 
            "Update Notification",
            JOptionPane.INFORMATION_MESSAGE
        );
        System.exit(0);
    }

    public boolean IsJGamePackageUpToDate() {
        assertProjectStatus("check if the JGame package is up to date");

        return projectRepresentation.jgamePackageVersion.equals(jgameSourceVersion);
    }
    
    public boolean IsStudioProject() {
        return new File(".jgame").exists();
    }

    public String GetJGameVersion() {
        assertProjectStatus("get the JGame package version");
        return projectRepresentation.jgamePackageVersion;
    }

    public ProjectRepresentation GetProjectRepresentation() {
        assertProjectStatus("get the project representation");
        return projectRepresentation;
    }

    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    private static void copyDir(String src, String dest, boolean overwrite) { //courtesy of stack overflow :)
        try {
            Files.walk(Paths.get(src)).forEach(a -> {
                Path b = Paths.get(dest, a.toString().substring(src.length()));
                try {
                    if (!a.toString().equals(src))
                        Files.copy(a, b, overwrite ? new CopyOption[] { StandardCopyOption.REPLACE_EXISTING }: new CopyOption[] {});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            // permission issue
            e.printStackTrace();
        }
    }
}
