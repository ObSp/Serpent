package JGamePackage.JGame.Classes.StudioClasses;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import JGamePackage.lib.CustomError.CustomError;
import JGamePackage.lib.JSONSimple.JSONArray;
import JGamePackage.lib.JSONSimple.JSONObject;
import JGamePackage.lib.JSONSimple.parser.JSONParser;
import JGamePackage.lib.JSONSimple.parser.ParseException;

public class ProjectRepresentation {
    public final File dotJGameDirectory;
    public final File jgamePackageDirectory;
    public final String jgamePackageVersion;
    public final ProjectJSONRepresentation projectJson;
    public JSONArray savedInstances = null;

    private static CustomError ErrorProjectReadingFailed = new CustomError("Failed to read the project.json file: %s.", CustomError.ERROR, "JGamePackage");

    public static ProjectJSONRepresentation CreateProjectJSONRepresentation(String path) {
        File f = new File(path);

        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(f);

            JSONObject baseObject = (JSONObject) parser.parse(reader);

            String name; String jgamePackageSource;
            name = (String) baseObject.get("name");
            jgamePackageSource = (String) baseObject.get("jgame_src");

            return new ProjectJSONRepresentation(path, jgamePackageSource, name);

        } catch (Exception e) {
            ErrorProjectReadingFailed.Throw(e.getMessage());
        }

        return null;
    }


    public ProjectRepresentation(File dotJGameDirectory, File jgamePackageDirectory, File projectJson) throws IOException {
        this.dotJGameDirectory = dotJGameDirectory;
        this.jgamePackageDirectory = jgamePackageDirectory;
        this.projectJson = CreateProjectJSONRepresentation(projectJson.getPath());
        
        //read package version
        List<String> allLines = Files.readAllLines(Paths.get(jgamePackageDirectory+"\\.version"));
        this.jgamePackageVersion = allLines.get(0);

        //read saved instances
        File savedInstancesFile = new File(dotJGameDirectory.getPath() + "\\world.json");
        JSONParser parser = new JSONParser();
        FileReader reader = new FileReader(savedInstancesFile);

        try {
            JSONArray baseObject = (JSONArray) parser.parse(reader);
            this.savedInstances = (JSONArray) baseObject;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public ProjectRepresentation() throws IOException {
        this(new File(".jgame"), new File("JGamePackage"), new File(".jgame\\project.json"));
    }

    @Override
    public String toString() {
        return "ProjectRepresentation [dotJGameDirectory=" + dotJGameDirectory + ", jgamePackageDirectory=" + jgamePackageDirectory + ", jgamePackageVersion=" + jgamePackageVersion + ", projectJson=" + projectJson + "]";
    }

    public static class ProjectJSONRepresentation {
        public final String path;
        public final String jgamePackageSource;
        public final String projectName;

        public ProjectJSONRepresentation(String path, String jgamePackageSource, String projectName) {
            this.path = path;
            this.jgamePackageSource = jgamePackageSource;
            this.projectName = projectName;
        }

        @Override
        public String toString() {
            return "ProjectJSONRepresentation [jgamePackageSource=" + jgamePackageSource + ", path=" + path + ", projectName=" + projectName + "]";
        }
        
    }
}
