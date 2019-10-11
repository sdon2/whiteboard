package canvas.layer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import name.LayerIdentifier;

import org.junit.Test;


public class LayerPropertiesTest {
    @Test
    public void testLayerIdentifier() {
        // test getting layerIdentifier
        LayerIdentifier layerId = new LayerIdentifier(5, "Layer");
        LayerProperties layerProperties = new LayerProperties(layerId);
        assertEquals(layerProperties.layerIdentifier(), layerId);
    }
    
    @Test
    public void testLayerOpacity() {
        // test getting opacity on constructed layerIdentifier
        LayerIdentifier layerId = new LayerIdentifier(5, "Layer");
        LayerProperties layerProperties = new LayerProperties(layerId);
        assertTrue(layerProperties.getOpacity() == 1.0);
    }
    
    @Test
    public void testSetLayerOpacity() {
        // test setting opacity on constructed layerIdentifier
        LayerIdentifier layerId = new LayerIdentifier(5, "Layer");
        LayerProperties layerProperties = new LayerProperties(layerId);
        layerProperties.setOpacity(.7);
        assertTrue(layerProperties.getOpacity() == 0.7);
    }
    
    @Test
    public void testLayerVisibility() {
        // test getting visibility on constructed layerIdentifier
        LayerIdentifier layerId = new LayerIdentifier(5, "Layer");
        LayerProperties layerProperties = new LayerProperties(layerId);
        assertTrue(layerProperties.getVisibility() == true);
    }
    
    @Test
    public void testSetLayerVisibility() {
        // test setting visibility on constructed layerIdentifier
        LayerIdentifier layerId = new LayerIdentifier(5, "Layer");
        LayerProperties layerProperties = new LayerProperties(layerId);
        layerProperties.setVisibility(false);
        assertTrue(layerProperties.getVisibility() == false);
    }
    
    @Test
    public void testToString() {
        // test getting visibility on constructed layerIdentifier
        LayerIdentifier layerId = new LayerIdentifier(5, "Layer");
        LayerProperties layerProperties = new LayerProperties(layerId);
        assertTrue(layerProperties.toString().equals("Layer"));
    }
    
    @Test
    public void testClone() {
        // test cloning
        LayerIdentifier layerId = new LayerIdentifier(5, "Layer");
        LayerProperties layerProperties = new LayerProperties(layerId);
        assertFalse(layerProperties == layerProperties.clone());
    }
    
    @Test
    public void testHashCode() {
        // test hashcode with two similar instances
        LayerIdentifier layerId = new LayerIdentifier(5, "Layer");
        LayerProperties layerProperties = new LayerProperties(layerId);
        LayerProperties layerProperties1 = new LayerProperties(layerId);
        assertTrue(layerProperties.hashCode() == layerProperties1.hashCode());
    }
    
    @Test
    public void testHashCodeDifferent() {
        // test hashcode with two dissimilar instances
        LayerIdentifier layerId = new LayerIdentifier(5, "Layer");
        LayerProperties layerProperties = new LayerProperties(layerId);
        LayerProperties layerProperties1 = new LayerProperties(layerId);
        layerProperties1.setOpacity(.4);
        assertTrue(layerProperties.hashCode() != layerProperties1.hashCode());
    }
}
