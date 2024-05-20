package org.example.model;

public class Note {
    public int octave;
    public int pitch;

    public Note(int octave, int pitch) {
        this.octave = octave;
        this.pitch = pitch;
    }

    public Note transpose(int semitones) {
        int newPitch = this.pitch + semitones;
        while (newPitch < 0) {
            newPitch += 12;
            this.octave--;
        }
        while (newPitch >= 12) {
            newPitch -= 12;
            this.octave++;
        }
        return new Note(this.octave, newPitch);
    }
}