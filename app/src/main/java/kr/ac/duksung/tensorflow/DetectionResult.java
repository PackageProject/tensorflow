package kr.ac.duksung.tensorflow;

import android.graphics.RectF;

public class DetectionResult {
    private RectF boundingBox;
    private String label;

    public DetectionResult(RectF boundingBox, String label) {
        this.boundingBox = boundingBox;
        this.label = label;
    }

    public RectF getBoundingBox() {
        return boundingBox;
    }

    public String getLabel() {
        return label;
    }
}
