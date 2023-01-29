package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.*;

// TODO music fucked up

/**
 * music player
 * panel with three labels that using mouse listeners can control music
 * reads some .wav music files
 */
class MusicPlayer extends JPanel {
    private final Game game;
    private final ArrayList<File> musicFiles;
    private final ArrayList<AudioInputStream> audioStreams;
    private final Clip clip;
    private int currentSong;
    private final int amountSongs = 5;
    private boolean intentionalStop = false;
    MusicPlayer(Game game)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        this.game = game;

        int width = this.game.getWidth();
        int height = this.game.getHeight();

        this.setBounds((int)(width*0.425), (int)(height*0.8),
                        (int)(width*0.15), (int)(height*0.08));
        this.setBackground(Color.LIGHT_GRAY);

        String path = "Images/MusicFiles/song";
        this.musicFiles = generateMusicFiles(path, this.amountSongs);

        this.audioStreams = generateAudioStreams(this.musicFiles);

        this.clip = AudioSystem.getClip();
        ClipLineListener clipLineListener = new ClipLineListener(this);
        this.clip.addLineListener(clipLineListener);

        this.currentSong = (int)(this.amountSongs*Math.random());

        BackwardMusicLabel backwardMusicLabel = new BackwardMusicLabel(this);
        this.add(backwardMusicLabel);

        PlayMusicLabel playMusicLabel = new PlayMusicLabel(this);
        this.add(playMusicLabel);

        ForwardMusicLabel forwardMusicLabel = new ForwardMusicLabel(this);
        this.add(forwardMusicLabel);
    }

    /**
     * method used to generate an arraylist that will contain
     * all the music files to play
     * @param path directory to the folder with files
     * @return ready arraylist with all the files
     */
    private ArrayList<File> generateMusicFiles(String path, int amountSongs) {
        ArrayList<File> result = new ArrayList<File>();

        for ( int i = 1; i <= amountSongs; i++ ) {
            String myPath = path + "";
            myPath += String.format("%02d", i);
            myPath += ".wav";
            File currFile = new File(myPath);
            result.add(currFile);
        }

        return result;
    }

    /**
     * method to operate on the clip to influence music
     * @param change "play" - play
     *               "pause" - pause
     *               "forward" - forward
     *               "backward" - backward
     *               "close" - close
     */
    public void changeClipState(String change) throws LineUnavailableException, IOException {
        if ( change.equals("play") ) {
            if ( !this.clip.isOpen() ) {
                this.clip.open(audioStreams.get(currentSong));
            }
            this.clip.start();
        } else if ( change.equals("pause") ) {
            this.clip.stop();
        } else if ( change.equals("close") ) {
            this.clip.stop();
            this.clip.close();
        } else System.out.println(change+" not understood");
    }

    /**
     * method that plays music if its paused
     * and pauses if its playing
     */
    public void playPause() throws LineUnavailableException, IOException {
        this.intentionalStop = true;
        if ( this.clip.isRunning() ) changeClipState("pause");
        else changeClipState("play");
        this.intentionalStop = false;
    }

    /**
     * method to play the next song
     * if the music is paused, it will just play the next song
     * @param wasRunning information about the state of the clip,
     *                   true if it was running while calling the command,
     *                   otherwise false
     */
    public void playForward(boolean wasRunning) throws LineUnavailableException, IOException {
        this.intentionalStop = true;
        this.currentSong++;
        if ( this.currentSong == this.amountSongs ) {
            this.currentSong = 0;
        }

        this.changeClipState("close");
        this.clip.open(this.audioStreams.get(this.currentSong));

        if ( wasRunning ) {
            this.changeClipState("play");
        }
        this.intentionalStop = false;
    }

    /**
     * method to play the previous song
     * if the music is paused, it will just change to the next song
     * @param wasRunning information about the state of the clip,
     *                   true if it was running while calling the command,
     *                   otherwise false
     */
    public void playBackward(boolean wasRunning) throws LineUnavailableException, IOException {
        this.intentionalStop = true;
        this.currentSong--;
        if ( this.currentSong == -1 ) {
            this.currentSong = this.amountSongs - 1;
        }

        changeClipState("close");
        this.clip.open(this.audioStreams.get(this.currentSong));

        if ( wasRunning ) {
            changeClipState("play");
        }
        this.intentionalStop = false;
    }

    /**
     * method that transforms ArrayList of music files into
     * arraylist of AudioInputStream objects
     * @param files arraylist of files to transform
     * @return arraylist with AudioInputStream object type
     * @throws UnsupportedAudioFileException has to be .wav
     * @throws IOException has to be correct file
     */
    private ArrayList<AudioInputStream> generateAudioStreams(ArrayList<File> files)
                throws UnsupportedAudioFileException, IOException {

        ArrayList<AudioInputStream> result = new ArrayList<AudioInputStream>();
        for (File currFile : files) {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(currFile);
            result.add(audioInputStream);
        }

        return result;
    }

    /**
     * method to check if the clip is currently running
     * @return true if is running, false if not
     */
    public boolean clipWasRunning() { return this.clip.isRunning(); }

    /**
     * getter for the intentional stop variable
     * @return this.intentionalStop of type boolean
     */
    public boolean getIntentionalStop() { return this.intentionalStop; }

    /**
     * listener used to play next song by the clip
     * when the next one finishes
     */
    static class ClipLineListener implements LineListener {
        private final MusicPlayer musicPlayer;
        ClipLineListener(MusicPlayer musicPlayer) {
            this.musicPlayer = musicPlayer;
        }
        @Override
        public void update(LineEvent event) {
            System.out.println("step1");
            if ( event.getType() != LineEvent.Type.STOP ) return;
            System.out.println("step2");
                if ( !this.musicPlayer.getIntentionalStop() ) return;
            System.out.println("step3");
            try {
                this.musicPlayer.playForward(true);
            } catch (LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * play/pause button for the music player
 * changes between icon when clicked
 */
class PlayMusicLabel extends JLabel {
    private final MusicPlayer musicPlayer;
    private final ArrayList<Icon> icons;
    public PlayMusicLabel(MusicPlayer nMusicPlayer)
            throws IOException {

        this.musicPlayer = nMusicPlayer;

        int width = this.musicPlayer.getWidth();
        int height = this.musicPlayer.getHeight();
        this.setBounds((int)(width*0.4), (int)(height*0.2),
                    (int)(width*0.2), (int)(height*0.9));

        String playPath = "Images/MusicPlayer/PlayButton.png";
        String pausePath = "Images/MusicPlayer/PauseButton.png";
        ImageIcon playImageIcon = new ImageIcon(playPath);
        ImageIcon pauseImageIcon = new ImageIcon(pausePath);
        Image tmpPlay = playImageIcon.getImage();
        Image tmpPause = pauseImageIcon.getImage();

        int iconWidth = this.getHeight();
        int iconHeight = this.getHeight();
        Image tmpPlay1 = tmpPlay.getScaledInstance(iconWidth,
                iconHeight, Image.SCALE_SMOOTH);
        Image tmpPause1 = tmpPause.getScaledInstance(iconWidth,
                iconHeight, Image.SCALE_SMOOTH);

        Icon playIcon = new ImageIcon(tmpPlay1);
        Icon pauseIcon = new ImageIcon(tmpPause1);

        ArrayList<Icon> icons = new ArrayList<Icon>();
        icons.add(playIcon);
        icons.add(pauseIcon);
        this.icons = icons;

        this.setIcon(this.icons.get(0));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);

        PlayMouseListener playML = new PlayMouseListener(this.musicPlayer, this);
        this.addMouseListener(playML);

        this.setVisible(true);
    }

    /**
     * method used to change icon from play to pause and
     * the other way
     */
    public void changeIcon() {
        /*
        if true - checks whether it's running
        if false - changes to the one not used right now
         */
        boolean byRunning = true;
        System.out.println(this.musicPlayer.clipWasRunning());
        if ( byRunning ) {
            if ( this.musicPlayer.clipWasRunning() ) {
                this.setIcon(this.icons.get(1));
            } else {
                this.setIcon(this.icons.get(0));
            }
        }
        else if ( !byRunning ) {
            Icon currIcon = this.getIcon();
            if (currIcon.equals(this.icons.get(0))) {
                this.setIcon(this.icons.get(1));
            } else {
                this.setIcon(this.icons.get(0));
            }
        }
    }

    /**
     * mouse listener that will react for clicking the play button
     * will
     *  - change the button to the other one
     *  - play/stop music
     */
    static class PlayMouseListener implements MouseListener {
        private final MusicPlayer musicPlayer;
        private final PlayMusicLabel playMusicLabel;

        PlayMouseListener(MusicPlayer nMusicPlayer, PlayMusicLabel playMusicLabel)
                throws IOException {

            this.musicPlayer = nMusicPlayer;
            this.playMusicLabel = playMusicLabel;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                this.musicPlayer.playPause();
            } catch (LineUnavailableException | IOException ex) {
                ex.printStackTrace();
            }
            this.playMusicLabel.changeIcon();
        }
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }
}

/**
 * button that allows the user to go to the next song
 */
class ForwardMusicLabel extends JLabel {
    private final MusicPlayer musicPlayer;
    public ForwardMusicLabel(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;

        ForwardMusicMouseListener forwardMusicML = new ForwardMusicMouseListener(this.musicPlayer);
        this.addMouseListener(forwardMusicML);

        int width = this.musicPlayer.getWidth();
        int height = this.musicPlayer.getHeight();

        this.setBounds((int)(width*0.7), (int)(height*0.2),
                (int)(width*0.2), (int)(height*0.9));

        String forwardPath = "Images/MusicPlayer/ForwardButton.png";
        ImageIcon forwardImageIcon = new ImageIcon(forwardPath);
        Image tmpForward = forwardImageIcon.getImage();

        int iconWidth = this.getHeight();
        int iconHeight = this.getHeight();
        Image tmpForawrd1 = tmpForward.getScaledInstance(iconWidth,
                iconHeight, Image.SCALE_SMOOTH);

        Icon forwardIcon = new ImageIcon(tmpForawrd1);

        this.setIcon(forwardIcon);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);

        this.setVisible(true);
    }

    static class ForwardMusicMouseListener implements MouseListener {
        private final MusicPlayer musicPlayer;
        public ForwardMusicMouseListener(MusicPlayer musicPlayer) {
            this.musicPlayer = musicPlayer;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                boolean wasRunning = this.musicPlayer.clipWasRunning();
                System.out.println("forward " + wasRunning);
                this.musicPlayer.playForward(wasRunning);
            } catch (LineUnavailableException | IOException ex) {
                ex.printStackTrace();
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }
}

/**
 * button that allows the user to go to the previous song
 */
class BackwardMusicLabel extends JLabel {
    private final MusicPlayer musicPlayer;
    public BackwardMusicLabel(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;

        BackwardMusicMouseListener backwardMusicML = new BackwardMusicMouseListener(this.musicPlayer);
        this.addMouseListener(backwardMusicML);

        int width = this.musicPlayer.getWidth();
        int height = this.musicPlayer.getHeight();

        this.setBounds((int)(width*0.1), (int)(height*0.2),
                (int)(width*0.2), (int)(height*0.9));

        String backwardPath = "Images/MusicPlayer/BackwardButton.png";
        ImageIcon backwardImageIcon = new ImageIcon(backwardPath);
        Image tmpBackward = backwardImageIcon.getImage();

        int iconWidth = this.getHeight();
        int iconHeight = this.getHeight();
        Image tmpBackward1 = tmpBackward.getScaledInstance(iconWidth,
                iconHeight, Image.SCALE_SMOOTH);

        Icon backwardIcon = new ImageIcon(tmpBackward1);

        this.setIcon(backwardIcon);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);

        this.setVisible(true);
    }

    static class BackwardMusicMouseListener implements MouseListener {
        private final MusicPlayer musicPlayer;
        public BackwardMusicMouseListener(MusicPlayer musicPlayer) {
            this.musicPlayer = musicPlayer;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                boolean wasRunning = this.musicPlayer.clipWasRunning();
                this.musicPlayer.playBackward(wasRunning);
            } catch (LineUnavailableException | IOException ex) {
                ex.printStackTrace();
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }
}
