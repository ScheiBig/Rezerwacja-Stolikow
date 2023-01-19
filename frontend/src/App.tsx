import { Route, Routes } from 'react-router';
import Header from './components/Header';
import Footer from './components/Footer';

export default function App(): React.ReactElement {

  return <>
    {/* <Header expanded/> */}
    <Header />
    <Routes>
    </Routes>
    <Footer />
  </>

}
