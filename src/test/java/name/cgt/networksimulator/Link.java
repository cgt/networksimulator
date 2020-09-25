package name.cgt.networksimulator;

interface Link {
    void send(Frame frame);

    void onConnected(FrameListener frameListener);
}
