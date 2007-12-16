/*
 * (c) Copyright 2007 by Richard Bergmair,
 *     See LICENSE.txt for terms and conditions
 *     for use, reproduction, and distribution.
 */

package org.ucam.semantilog.simplevocab;

import java.io.*;

import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.HashSet;



public class SRCard implements Serializable
  {
    static final long serialVersionUID = 0;
    
    public static final int STATUS_BASE = 0;
    public static final int STATUS_FAILED = 1;
    public static final int STATUS_SUCCESS = 2;
    
    String stimMsg;
    String beforeCorrection;
    
    HashMap responses;
    Vector allGroups;
    Vector groupReps;
    
    Vector groups;
    String respMsg;

    int status;
    
    
    
    public SRCard( String stimulus, String response )
      {
        this.responses = new HashMap();
        this.allGroups = new Vector();
        this.groupReps = new Vector();
        this.beforeCorrection = null;
        
        StringBuffer strbuf = new StringBuffer();
        StringTokenizer strTok = new StringTokenizer( stimulus, "," );
        while ( strTok.hasMoreTokens() )
          {
            strbuf.append( strTok.nextToken().trim() );
            strbuf.append( "\n" );
          }
        stimMsg = strbuf.toString();

        StringTokenizer strTok1 = new StringTokenizer( response, ";" );
        while ( strTok1.hasMoreTokens() )
          {
            String respItem = strTok1.nextToken();
            Integer sz = new Integer( allGroups.size() );
            allGroups.add( new Boolean(false) );
            groupReps.add( respItem.trim() );
            StringTokenizer strTok2 = new StringTokenizer( respItem, "," );
            while ( strTok2.hasMoreTokens() )
              {
                String st = strTok2.nextToken().trim();
                Iterator it = strHashCode( st ).iterator();
                while ( it.hasNext() )
                  {
                    Integer key = ( Integer )it.next();
                    Vector vec = (Vector)( responses.get(key) );
                    if ( vec == null )
                      vec = new Vector();
                    vec.add( st );
                    vec.add( sz );
                    responses.put( key, vec );
                  }
              }
          }
        initState();
      }
    
    public SRCard( SRCard src )
      {
        this.stimMsg = src.stimMsg;
        this.responses = src.responses;
        this.allGroups = src.allGroups;
        this.groupReps = src.groupReps;
        this.beforeCorrection = null;
        initState();
      }
    
    private void initState()
      {
        groups = new Vector( allGroups );
        respMsg = "";
        status = STATUS_BASE;
      }
    
    

    public int getStatus()
      {
        return status;
      }
    
    public String getStimMsg()
      {
        return stimMsg;
      }

    public String getRespMsg()
      {
        return respMsg;
      }
    
    public HashSet getCharacters()
      {
        HashSet set = new HashSet();
        Iterator it1 = responses.values().iterator();
        while ( it1.hasNext() )
          {
            Vector resps = ( Vector )it1.next();
            Iterator it2 = resps.iterator();
            while ( it2.hasNext() )
              {
                String resp = ( String )it2.next();
                it2.next();
                for ( int i = 0; i < resp.length(); i++ )
                  {
                    // int j = resp.codePointAt( i );
                    int j = ( int )( resp.charAt(i) );
                    // System.out.println( j );
                    if ( j > 0x7F )
                      set.add( new Character( resp.charAt( i ) ) );
                  }
              }
          }
        return set;
      }



    private Vector strHashCode( String st )
      {
        Vector rslt = new Vector();
        st = st.replaceAll( "\\(.*\\)", "" );
        String st2 = st.replaceAll( "\\[.*\\]", "" );
        String st3 = st.replaceAll( "\\[", "" ).replaceAll( "\\]", "" );
        int hc2 = st2.trim().hashCode();
        rslt.add( new Integer( hc2 ) );
        int hc3 = st3.trim().hashCode();
        if ( hc3 != hc2 )
          rslt.add( new Integer( hc3 ) ); 
        return rslt;
      }
    
    private void succeed( int gid )
      {
        if ( !( (Boolean)groups.get(gid) ).booleanValue() )
          {
            groups.remove( gid );
            groups.add( gid, new Boolean( true ) );
            
            respMsg += groupReps.get( gid ) + "\n";
          }
        
        Iterator it = groups.iterator();
        while (it.hasNext())
          if (!(( Boolean )it.next()).booleanValue())
            return;
        
        status = STATUS_SUCCESS;
      }
    
    private void fail()
      {
        StringBuffer strb = new StringBuffer();
        Iterator it = groupReps.iterator();
        while (it.hasNext())
          {
            strb.append( (String)it.next() );
            strb.append( "\n" );
          }
        this.beforeCorrection = respMsg;
        respMsg = strb.toString();
        status = STATUS_FAILED;
      }
    
    public void reset()
      {
        respMsg = this.beforeCorrection;
        this.beforeCorrection = null;
        status = STATUS_BASE;
      }
    

    
    public void respond( String response )
      {
        Vector vec = ( Vector )responses.get( strHashCode( response ).get( 0 ) );
        if ( ( vec == null ) || ( response.equals("") ) )
          {
            fail();
            return;
          }
        
        Iterator it = vec.iterator();
        
        while ( it.hasNext() )
          {
            String st = ( String )it.next();
            int gid = ( (Integer)it.next() ).intValue();
            
            if ( st == null )
              {
                fail();
                return;
              }
    
            String resp = response.trim();
            if ( st.equals( resp ) )
              {
                succeed( gid );
                return;
              }
              
            if ( st.indexOf( "(" ) != -1 )
              {
                st = st.replaceAll( "\\(.*\\)", "" ).trim();
                if ( st.equals( resp ) )
                  {
                    succeed( gid );
                    return;
                  }
              }
            
            if ( st.indexOf( "[" ) != -1 )
              {
                String st2 = st.replaceAll( "\\[", "" ).replaceAll( "\\]", "" ).trim();
                if ( st2.equals( resp ) )
                  {
                    succeed( gid );
                    return;
                  }
                st = st.replaceAll( "\\[.*\\]", "" ).trim();
                if ( st.equals( resp ) )
                  {
                    succeed( gid );
                    return;
                  }
              }
          }
        fail();
      }
  }

/*
 * (c) Copyright 2007 by Richard Bergmair,
 *     See LICENSE.txt for terms and conditions
 *     for use, reproduction, and distribution.
 */
