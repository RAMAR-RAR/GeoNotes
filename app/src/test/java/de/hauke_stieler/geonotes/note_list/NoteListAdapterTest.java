package de.hauke_stieler.geonotes.note_list;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import de.hauke_stieler.geonotes.R;
import de.hauke_stieler.geonotes.notes.Note;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class NoteListAdapterTest {

    private NoteListAdapter adapter;

    private Context context;
    private Resources resourcesMock;
    private View viewMock;
    private ImageView imageViewMock;
    private TextView textViewMock;
    private List<Note> notes;
    private List<Note> notesWithPhotos;
    private NoteListAdapter.NoteListClickListener clickListenerMock;

    @Before
    public void setup() {
        context = Mockito.mock(Context.class);
        Mockito.when(context.getSystemService(anyString())).thenReturn(null);

        resourcesMock = Mockito.mock(Resources.class);
        Mockito.when(context.getResources()).thenReturn(resourcesMock);

        viewMock = Mockito.mock(View.class);

        imageViewMock = Mockito.mock(ImageView.class);
        Mockito.when(viewMock.findViewById(R.id.note_list_row_icon)).thenReturn(imageViewMock);

        textViewMock = Mockito.mock(TextView.class);
        Mockito.when(viewMock.findViewById(R.id.note_list_row_text_view)).thenReturn(textViewMock);

        notes = new ArrayList<>();
        notes.add(new Note(123L, "foo", 12, 23, "now"));
        notes.add(new Note(234L, "bar", 34, 45, "i don't remember"));
        notes.add(new Note(345L, "", 56, 56, "tomorrow"));

        notesWithPhotos = new ArrayList<>();
        notesWithPhotos.add(notes.get(1));
        notesWithPhotos.add(notes.get(2));

        clickListenerMock = Mockito.mock(NoteListAdapter.NoteListClickListener.class);

        adapter = new NoteListAdapter(context, notes, notesWithPhotos, clickListenerMock);
    }

    @Test
    public void testGetCount() {
        // Act
        int count = adapter.getCount();

        // Assert
        Assert.assertEquals(notes.size(), count);
    }

    @Test
    public void testGetItems() {
        // Act & Assert
        for (int i = 0; i < notes.size(); i++) {
            Note item = adapter.getItem(i);

            Assert.assertEquals(notes.get(i), item);
        }
    }

    @Test
    public void testGetItemIds() {
        // Act & Assert
        for (int i = 0; i < notes.size(); i++) {
            long itemId = adapter.getItemId(i);

            Assert.assertEquals(notes.get(i).getId(), itemId);
        }
    }

    @Test
    public void testCreatingView_withoutPhoto() {
        // Act
        View view = adapter.getView(0, viewMock, null);

        // Assert
        Assert.assertEquals(viewMock, view);
        Mockito.verify(imageViewMock).setImageResource(R.mipmap.ic_note);
        Mockito.verifyNoMoreInteractions(imageViewMock);
        Mockito.verify(textViewMock).setText(notes.get(0).getDescription());
        Mockito.verify(textViewMock).setOnClickListener(any());
        Mockito.verifyNoMoreInteractions(textViewMock);
    }

    @Test
    public void testCreatingView_withPhoto() {
        // Act
        View view = adapter.getView(1, viewMock, null);

        // Assert
        Assert.assertEquals(viewMock, view);
        Mockito.verify(imageViewMock).setImageResource(R.mipmap.ic_note_photo);
        Mockito.verifyNoMoreInteractions(imageViewMock);
        Mockito.verify(textViewMock).setText(notes.get(1).getDescription());
        Mockito.verify(textViewMock).setOnClickListener(any());
        Mockito.verifyNoMoreInteractions(textViewMock);
    }

    @Test
    public void testCreatingView_withPhotoOnly() {
        // Arrange
        int colorCode = 123;
        Mockito.when(resourcesMock.getColor(R.color.grey)).thenReturn(colorCode);

        // Act
        View view = adapter.getView(2, viewMock, null);

        // Assert
        Assert.assertEquals(viewMock, view);
        Mockito.verify(imageViewMock).setImageResource(R.mipmap.ic_note_photo);
        Mockito.verifyNoMoreInteractions(imageViewMock);
        Mockito.verify(textViewMock).setText("(only photo)");
        Mockito.verify(textViewMock).setTypeface(null, Typeface.ITALIC);
        Mockito.verify(textViewMock).setTextColor(colorCode);
        Mockito.verify(textViewMock).setOnClickListener(any());
        Mockito.verifyNoMoreInteractions(textViewMock);
    }

    @Test
    public void testClickOnTextView() {
        // Arrange
        int noteIndex = 1;
        adapter.getView(noteIndex, viewMock, null);

        ArgumentCaptor<View.OnClickListener> clickListenerArgumentCaptor = ArgumentCaptor.forClass(View.OnClickListener.class);
        Mockito.verify(textViewMock).setOnClickListener(clickListenerArgumentCaptor.capture());

        // Act
        clickListenerArgumentCaptor.getValue().onClick(textViewMock); // simulate the click by manually executing event listener

        // Assert
        Mockito.verify(clickListenerMock).onClick(notes.get(noteIndex).getId());
    }
}
