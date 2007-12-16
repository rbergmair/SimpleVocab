/*
 * (c) Copyright 2007 by Richard Bergmair,
 *     See LICENSE.txt for terms and conditions
 *     for use, reproduction, and distribution.
 */

package org.ucam.semantilog.simplevocab.gui;

import javax.swing.table.DefaultTableModel;

public class VocabGUICharPaletteTableModel extends DefaultTableModel
{
  static final long serialVersionUID = 0;
  
  public VocabGUICharPaletteTableModel()
    {
      super( 4, 15 );
    }
  
  public boolean isCellEditable( int row, int column )
    {
      return false;
    }
}

/*
 * (c) Copyright 2007 by Richard Bergmair,
 *     See LICENSE.txt for terms and conditions
 *     for use, reproduction, and distribution.
 */
