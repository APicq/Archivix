package apicq.archivix.gui;

/**
 * this class is wrapper around an attachment name and its md5
 */
public class AttachmentSignature {

    private final String md5 ;

    public String getMd5() {
        return md5;
    }

    public String getName() {
        return name;
    }

    private final String name ;


    public AttachmentSignature(String md5, String name) {
        this.md5 = md5;
        this.name = name;
    }
}
