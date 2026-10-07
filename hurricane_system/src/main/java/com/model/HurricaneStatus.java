package com.model;

public enum HurricaneStatus {
    INCOMING, IN_PROGRESS, OVER;
/**
 * Where a hurricane is in its lifecycle.
 *
 * <p>A storm moves forward through these states and never goes back: an
 * admin adds it as {@link #INCOMING}, it becomes {@link #IN_PROGRESS} once
 * it makes landfall, and {@link com.model.Hurricane#endHurricane()} sets it
 * to {@link #OVER} when it passes.</p>
 *
 * <p>The status decides whether a storm still matters for alerts. Only a
 * storm that is INCOMING or IN_PROGRESS can put users in danger.</p>
 *
 * @author SynthwaveFox
 */
}
