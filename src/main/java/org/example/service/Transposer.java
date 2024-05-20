package org.example.service;

import org.example.model.Note;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Transposer {
    private static final int MIN_OCTAVE = -3;
    private static final int MAX_OCTAVE = 5;
    private static final int MIN_NOTE_NUM = 1;
    private static final int MAX_NOTE_NUM = 12;

    private static String filePath;
    private static int semitone;
    private static String outputFilePath;

    // Function to parse the input file and return a list of notes
    private List<int[]> parseInputFile(String filePath) throws IOException {
        List<int[]> notes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.replaceAll("\\[", "").replaceAll("\\]", "").split(",");
                for (int i = 0; i < lines.length; i += 2) {
                    int octave = Integer.parseInt(lines[i].trim());
                    int note = Integer.parseInt(lines[i + 1].trim());
                    notes.add(new int[]{octave, note});
                }
            }
        }
        return notes;
    }

    // Function to transpose a single note
    private static List<Integer> transposeNote(int[] note, int semitones) {
        int octave = note[0];
        int noteNum = note[1];

        int transposedNoteNum = (noteNum - semitones) % 12;

        if (transposedNoteNum <= 0) {
            octave--;
            transposedNoteNum += 12;
        }

        if (octave < MIN_OCTAVE || octave > MAX_OCTAVE ||
                (octave == MIN_OCTAVE && transposedNoteNum < MIN_NOTE_NUM) ||
                (octave == MAX_OCTAVE && transposedNoteNum > MAX_NOTE_NUM)) {
            throw new IllegalArgumentException("Resulting note falls out of keyboard range");
        }

        List<Integer> transposedNote = new ArrayList<>();
        transposedNote.add(octave);
        transposedNote.add(transposedNoteNum);
        return transposedNote;
    }

    // Function to transpose a musical piece
    private static List<List<Integer>> transposePiece(List<int[]> piece, int semitones) {
        List<List<Integer>> transposedPiece = new ArrayList<>();
        for (int[] note : piece) {
            transposedPiece.add(transposeNote(note, semitones));
        }
        return transposedPiece;
    }



    private void writeOutputFile(List<List<Integer>> notes, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.print("[");
            for (List<Integer> note : notes) {
                writer.print("[");
                for (int i = 0; i < note.size(); i++) {
                    writer.print(note.get(i));
                    if (i < note.size() - 1) {
                        writer.print(",");
                    }
                }
                writer.print("]");
                if (note != notes.get(notes.size() - 1)) {
                    writer.print(",");
                }
            }
            writer.print("]");
        }
    }

    private  boolean isInKeyboardRange(Note note) {
        return note.octave >= -3 && note.octave <= 5 && note.pitch >= 1 && note.pitch <= 12;
    }

    private void handleArguments(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java -jar Assignment.jar <inputFile> <semitone> <outputFile>");
            System.exit(1);
        }

        filePath = args[0];
        System.out.println("filePath -> "+filePath);
        semitone = Integer.parseInt(args[1]);
        System.out.println("semitone -> "+semitone);
        outputFilePath = args[2];
        System.out.println("outputFilePath -> "+outputFilePath);
    }

    public void execute(String []args){
        handleArguments(args);

        try {
            List<int[]> notes = parseInputFile(filePath);
            List<List<Integer>> transposedNotes = transposePiece(notes, semitone);

            for (List<Integer> note : transposedNotes) {
                Note n = new Note(note.get(0), note.get(1));
                if (!isInKeyboardRange(n)) {
                    System.err.println("Error: At least one of the input or resulting notes falls out of the keyboard range");
                    return;
                }
            }

            writeOutputFile(transposedNotes, outputFilePath);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
    }

