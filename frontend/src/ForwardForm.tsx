import React, {useState} from 'react';
import axios from 'axios';

export default function ForwardForm(){
  const [buyCurrency,setBuyCurrency]=useState('USD');
  const [sellCurrency,setSellCurrency]=useState('INR');
  const [amount,setAmount]=useState('1000');
  const [rate,setRate]=useState('83.5');
  const [tenor,setTenor]=useState('1M');
  const [msg,setMsg]=useState('');

  const submit = async ()=>{
    try{
      const body = {buyCurrency,sellCurrency,sellAmount: parseFloat(amount), rate: parseFloat(rate), customerId:'CUST1', tenor};
      const resp = await axios.post('/api/trades/forward', body, {headers:{'X-API-KEY':'test-key'}});
      setMsg('Created trade id ' + resp.data.id);
    }catch(e:any){ setMsg('Error: '+ (e.response?.data?.message || e.message)); }
  }

  return (<div>
    <h3>Forward Trade</h3>
    <div>
      <label>Buy</label>
      <input value={buyCurrency} onChange={e=>setBuyCurrency(e.target.value)} />
      <label>Sell</label>
      <input value={sellCurrency} onChange={e=>setSellCurrency(e.target.value)} />
    </div>
    <div>
      <label>Amount</label>
      <input value={amount} onChange={e=>setAmount(e.target.value)} />
      <label>Rate</label>
      <input value={rate} onChange={e=>setRate(e.target.value)} />
      <label>Tenor</label>
      <input value={tenor} onChange={e=>setTenor(e.target.value)} />
    </div>
    <button onClick={submit}>Submit Forward</button>
    <div>{msg}</div>
  </div>)
}
