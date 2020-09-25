package name.cgt.networksimulator;

interface Link extends FrameListener {
    void send(Frame frame);

    void onConnected(FrameListener frameListener);
}
