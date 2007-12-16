/*
 * (c) Copyright 2007 by Richard Bergmair,
 *     See LICENSE.txt for terms and conditions
 *     for use, reproduction, and distribution.
 */

package org.ucam.semantilog.simplevocab.gui;

import javax.swing.*;

import org.ucam.semantilog.simplevocab.Config;
import org.ucam.semantilog.simplevocab.VocabTrainer;
// import javax.swing.table.*;

import java.awt.*;
import java.util.*;

import java.awt.event.*;


public class VocabGUICharPalette extends JTable implements MouseListener
  {
    static final long serialVersionUID = 0;
    protected  int rowHeight = 100;
    
        VocabGUIFrame vgf;
    
    public VocabGUICharPalette( VocabGUIFrame vgf )
      {
        super( new VocabGUICharPaletteTableModel() );
        
        this.vgf = vgf;
        Font dspfnt = Font.decode( Config.getInstance().getGUIFont() );
        setFont( dspfnt );
        
        for ( int i=0; i<4; i++ )
          setRowHeight( i, ( int )( dspfnt.getSize()*1.2 ) );
        
        setCellSelectionEnabled( true ); 
        setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        addMouseListener( this );
        
        refresh();
      }
    
    public void refresh()
      {
        if ( ( vgf.vocabTrainer != null ) && ( vgf.vocabTrainer.getStatus() == VocabTrainer.STATUS_VOCAB ) )
          {
            Set chars = vgf.vocabTrainer.getCharacters( 60 );
            Iterator it = chars.iterator();
            for ( int i=0; i<60; i++ )
              {
                if (!it.hasNext())
                  break;
                Character ch = ( Character )it.next();
                setValueAt( ch, i / 15, i % 15 );
                /*
                JLabel lbl = ( JLabel )getComponent( i );
                lbl.setText( ch.toString() );
                */
              }
          }
      }
    
    public void mouseClicked(MouseEvent e)
      {
        if ( e.getClickCount() == 2 )
          vgf.appendChar( ( Character )getModel().getValueAt( getSelectedRow(), getSelectedColumn() ) );
      }
    
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
}

/*
 * (c) Copyright 2007 by Richard Bergmair,
 *     See LICENSE.txt for terms and conditions
 *     for use, reproduction, and distribution.
 */
