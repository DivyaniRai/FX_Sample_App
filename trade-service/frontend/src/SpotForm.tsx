import React, {useState} from 'react';
import axios from 'axios';

export default function SpotForm(){
  const [buyCurrency,setBuyCurrency]=useState('EUR');
  const [sellCurrency,setSellCurrency]=useState('USD');
  const [amount,setAmount]=useState('1000');
  const [rate,setRate]=useState('1.1');
  const [counterparty,setCounterparty]=useState('citi');
  const [msg,setMsg]=useState('');

  const submit = async ()=>{
    try{
      const body = {buyCurrency,sellCurrency,buyAmount: parseFloat(amount), rate: parseFloat(rate), customerId:'CUST1', counterparty};
      const resp = await axios.post('/api/trades/spot', body, {headers:{'X-API-KEY':'test-key'}});
      setMsg('Created trade id ' + resp.data.id);
    }catch(e:any){ setMsg('Error: '+ (e.response?.data?.message || e.message)); }
  }

  return (<div>
    <h3>Spot Trade</h3>
    <div style={{display:'flex', gap:'8px', alignItems:'center'}}>
      <label>Buy</label>
      <input value={buyCurrency} onChange={e=>setBuyCurrency(e.target.value)} />
      <label>Sell</label>
      <input value={sellCurrency} onChange={e=>setSellCurrency(e.target.value)} />
    </div>
    <div style={{display:'flex', gap:'8px', alignItems:'center'}}>
      <label>Amount</label>
      <input value={amount} onChange={e=>setAmount(e.target.value)} />
      <label>Rate</label>
      <input value={rate} onChange={e=>setRate(e.target.value)} />
    </div>
    <div style={{textAlign:'left', margin:'8px 0'}}>
      <label>Counterparty</label>
      <select value={counterparty} onChange={e=>setCounterparty(e.target.value)}>
        <option value="citi">Citi</option>
        <option value="scb">SCB</option>
        <option value="hdfc">HDFC</option>
      </select>
    </div>
    <button onClick={submit}>Submit Spot</button>
    <div>{msg}</div>
  </div>)
}
