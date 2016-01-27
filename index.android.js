'use strict';
import React, {
  AppRegistry,
  Component,
  StyleSheet,
  Text,
  Image,
  TouchableHighlight,
  View,
  TextInput
} from 'react-native';

import ReacherModuleJS from './ReacherModuleJS';

class ImageSwitcher extends Component{

  render() {
    if (this.props.condition === 'uncheked') {
      return (
        <Image style={this.props.style} source={require('./img/disabled.png')}/>
      );
    }
    if (this.props.condition === 'success') {
      return (
        <Image style={this.props.style} source={require('./img/green.png')}/>
      );
    }
    return (
      <Image style={this.props.style} source={require('./img/red.png')}/>
    );
  }

}

class ReacherAndroid extends Component {
  constructor(props) {
    super(props);
    this.state = {
      text: '',
      condition: 'uncheked', /*['uncheked', 'success', 'fail']*/
    };
  }

  _onPressButton() {
    ReacherModuleJS.reach(this.state.text, (err, data) => {
      if (err) return;
      console.log(data);
      this.setState({condition: data ? 'success' : 'fail'})
    });
  }

  render() {
    return (
      <View style={styles.container}>
        <TextInput
          style={styles.textInput}
          onChangeText={(text) => this.setState({text})}
          value={this.state.text}/>
        <View style={styles.footer}>
          <ImageSwitcher
            style={styles.image}
            condition={this.state.condition}/>
          <TouchableHighlight
            style={styles.button}
            onPress={this._onPressButton.bind(this)}>
            <Text style={{color: 'white'}}>Test</Text>
          </TouchableHighlight>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F5FCFF',
    flexDirection: 'column',
    justifyContent: 'flex-start'
  },
  footer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginTop: 5,
  },
  image: {
    marginLeft: 10,
  },
  button: {
    backgroundColor: 'grey',
    justifyContent: 'center',
    alignItems: 'center',
    width: 80,
    height: 35,
    marginRight: 10,
  },
  textInput: {
    height: 40,
    borderColor: 'gray',
    borderWidth: 1
  }
});


AppRegistry.registerComponent('ReacherAndroid', () => ReacherAndroid);
