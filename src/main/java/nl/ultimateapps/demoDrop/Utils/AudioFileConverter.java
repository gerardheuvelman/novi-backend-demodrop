package nl.ultimateapps.demoDrop.Utils;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

public class AudioFileConverter {

//    public static void convertToMP3(File inputFile, File outputFile)
//            throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, CannotWriteException {
//
//        AudioFile audioFile = AudioFileIO.read(inputFile);
//        AudioHeader audioHeader = audioFile.getAudioHeader();
//
//        MP3File mp3File = new MP3File(inputFile);
//        mp3File. setBitRate(320);
//        mp3File.commit();
//
//        AudioFileIO.write(mp3File, outputFile);
//    }
}

