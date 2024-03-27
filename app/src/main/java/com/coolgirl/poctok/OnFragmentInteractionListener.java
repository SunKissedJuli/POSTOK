package com.coolgirl.poctok;

public interface OnFragmentInteractionListener {
    void onOpenFragmentRequested(String FragmentName, int id);
    void onOpenFragmentRegistration(String login, String password);
    void onOpenNoteFragment(int id, int noteId, String noteText, String noteData, int plantId);
    void onOpenPlantFragment(int id, int plantId);
}
