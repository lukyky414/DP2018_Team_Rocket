package controller;

import dao.WrongSaveException;
import dao.XmlDAO;
import main.Main;
import textureFactory.*;
import view.VueJeu;
import view.VueMenuBar;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;

public class EnregistrerChargerListener implements ActionListener {

    private JFrame fenetre;
    private VueMenuBar vmb;
    private VueJeu vj;

    private JFileChooser chooser;
    private XmlDAO xmlDao;

    public EnregistrerChargerListener(JFrame fenetre, VueJeu vj, VueMenuBar vmb) {
        this.fenetre=fenetre;
        this.vj=vj;
        this.vmb=vmb;
        this.chooser = new JFileChooser();
        this.chooser.setFileFilter(new FileNameExtensionFilter("XML File","xml"));
        this.chooser.setSelectedFile(new File("sauvegarde.xml"));
		this.chooser.setCurrentDirectory(Paths.get("res/sav/").toAbsolutePath().toFile());
        this.xmlDao = new XmlDAO();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String test=e.getActionCommand();
        switch (test) {
            case "Sauvegarder":
                enregistrer();
                break;
            case "Charger":
                charger();
                break;
            default:
        }
    }

    private void enregistrer() {
        File f=selectFile(false);

        if (f != null) {
            this.xmlDao.save(f.getPath());
        }
    }

    private void charger() {
        File f=selectFile(true);
        if (f != null) {
            try {
                this.xmlDao.load(f.getPath());
            } catch (WrongSaveException e) {
                e.printStackTrace();
            }
        }
        VueJeu.setEnabled(vj,true);
        vj.update();
        vj.ajouterTirerListener();
        vmb.peutSauvegarder(true);
    }

    public File selectFile(boolean load) {
        int returnVal;
        if (load) {
            returnVal=chooser.showOpenDialog(fenetre);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                return chooser.getSelectedFile();
            } else {
                return null;
            }
        } else {
            returnVal=chooser.showSaveDialog(fenetre);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                String filename = chooser.getSelectedFile().toString();
                if (!filename .endsWith(".xml")) {
                    filename += ".xml";
                }
                return new File(filename);
            } else {
                return null;
            }
        }
    }
}
