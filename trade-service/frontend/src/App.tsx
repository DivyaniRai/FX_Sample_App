import React from 'react';
import SpotForm from './SpotForm';
import ForwardForm from './ForwardForm';
import Blotter from './Blotter';
import RateManager from './RateManager';

export default function App(){
  return (
    <div style={{padding:20}}>
      <h1>FX Practice App</h1>
      <div style={{display:'flex',gap:20}}>
        <div style={{flex:1}}>
          <SpotForm />
          <ForwardForm />
        </div>
        <div style={{flex:1}}>
          <Blotter />
          <RateManager />
        </div>
      </div>
    </div>
  )
}
