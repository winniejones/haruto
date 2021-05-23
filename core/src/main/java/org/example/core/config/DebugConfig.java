package org.example.core.config;

public class DebugConfig {
    public boolean drawDebugUI;
    public boolean drawFPS;
    public boolean drawDiagnosticInfo;
    public boolean drawComponentList;
    public boolean drawPos;
    public boolean box2DDebugRender;
    public boolean drawBodies;
    public boolean drawJoints;
    public boolean drawAABBs;
    public boolean drawInactiveBodies;
    public boolean drawVelocities;
    public boolean drawContacts;
    public boolean drawOrbitPath;
    public boolean drawMousePos;
    public boolean drawEntityList;

    public DebugConfig() {
        drawDebugUI = true;
        drawFPS = true;
        drawDiagnosticInfo = true;
        drawComponentList = false;
        drawPos = false;
        box2DDebugRender = true;
        drawBodies = true;
        drawJoints = true;
        drawAABBs = true;
        drawInactiveBodies = true;
        drawContacts = true;
        drawMousePos = false;
        drawEntityList = true;
    }
}
