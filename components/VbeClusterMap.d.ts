import * as React from "react";
import PropTypes from 'prop-types';
import { StyleProp, ViewStyle } from "react-native";

export interface Region {
  latitude: number;
  longitude: number;
  latitudeDelta: number;
  longitudeDelta: number;
  zoom: number;
}

export interface LatLng {
  latitude: number;
  longitude: number;
}

export interface ClusterData {
  id: number;
  siteName: string;
  latlng: LatLng;
  available: boolean;
}

export interface VbeClusterMapProps {
  style?: StyleProp<ViewStyle>;
  region: Region;
  data: ClusterData[];
  animation: boolean;
  showUserLocation: boolean;
  moveOnMarkerPress: boolean;
  onMapReady?: () => void;
  onMarkerPress?: (event: ClusterData) => void;
}

export declare class VbeClusterMap extends React.Component<VbeClusterMapProps, any> {
  static propTypes: {
    style: PropTypes.Requireable<any>,
    region: PropTypes.Requireable<Region>,
    data: PropTypes.Requireable<ClusterData[]>,
    animation: PropTypes.Requireable<boolean>,
    showUserLocation: PropTypes.Requireable<boolean>,
    moveOnMarkerPress: PropTypes.Requireable<boolean>,
    onMapReady: PropTypes.Requireable<() => any>,
    onMarkerPress: PropTypes.Requireable<(...args: ClusterData[]) => any>
  };
  static defaultProps: {
    animation: boolean;
    showUserLocation: boolean;
    moveOnMarkerPress: boolean;
  }
}

export default VbeClusterMap