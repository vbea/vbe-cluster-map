import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { View, ViewPropTypes, requireNativeComponent } from 'react-native';

const viewPropTypes = ViewPropTypes || View.propTypes;

const propTypes = {
  ...viewPropTypes,
  /**
   * Callback that is called continuously when the user is dragging the map.
   */
  style: viewPropTypes.style,
  region: PropTypes.shape({
    latitude: PropTypes.number.isRequired,
    longitude: PropTypes.number.isRequired,
    latitudeDelta: PropTypes.number.isRequired,
    longitudeDelta: PropTypes.number.isRequired,
    zoom: PropTypes.number
  }),
  data: PropTypes.array,
  animation: PropTypes.bool,
  showUserLocation: PropTypes.bool,
  moveOnMarkerPress: PropTypes.bool,
  onMapReady: PropTypes.func,
  onMarkerPress: PropTypes.func
};

const VbeClusterView = requireNativeComponent('VbeCluster', propTypes);

class VbeClusterMap extends Component {

  constructor(props) {
    super(props);
    this.state = {
      mapReady: false
    }
    this.map;
    this._onMapReady = this._onMapReady.bind(this);
    this._onMarkerPress = this._onMarkerPress.bind(this);
    this.moveCamera = this.moveCamera.bind(this);
    this.showUserLocation = this.showUserLocation.bind(this);
  }

  _onMapReady() {
    this.setState({
      mapReady: true
    })
    /*if (this.props.region) {
      this.map.setNativeProps({ region: this.props.region });
    }*/
    if (this.props.onMapReady) {
      this.props.onMapReady();
    }
  }

  _onMarkerPress(event) {
    if (this.props.onMarkerPress) {
      this.props.onMarkerPress(event.nativeEvent);
    }
  }

  moveCamera(region, zoom) {
    //console.log('moveCamera', region);
    if (region) {
      if (!region.zoom && zoom) {
        region.zoom = zoom;
      }
      this.map.setNativeProps({ region: region });
    }
  }

  showUserLocation() {
    this.map.setNativeProps({ showUserLocation: this.props.showUserLocation });
  }
  
  render() {
    return (
      <VbeClusterView
        ref={ref => this.map = ref}
        {...this.props}
        onMapReady={this._onMapReady}
        onMarkerPress={this._onMarkerPress}
      />
    );
  }
}

VbeClusterMap.propTypes = propTypes;

module.exports = VbeClusterMap;