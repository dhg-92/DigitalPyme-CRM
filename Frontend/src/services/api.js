import axios from 'axios';
import router from '../router';

export const api = axios.create({
  baseURL: 'http://localhost:8085',
});

api.interceptors.request.use(config => {

  if (config.url.includes('/users/users/login')) {
    return config;
  }

  const token = localStorage.getItem('access_token');
  const expirationDate = localStorage.getItem('expires_in');

  if (token && expirationDate && new Date().getTime() < expirationDate) {
    config.headers.Authorization = `Bearer ${token}`;
    return config;
  }

  router.push('/logout');
  return config;
}, error => {
  return Promise.reject(error);
});

export const login = (data) => {
  return api.post('/users/users/login', data);
};

export const setRestorePassword = (data) => {
  return api.post('/users/users/restorePassword', data);
};

export const setPassword = (data) => {
  return api.post('/users/users/setPassword', data);
};

export const userInfo = () => {
  return api.get('/users/users/info');
};

export const getAllOffers = () => {
  return api.get('/offers/offers');
};

export const getOfferById = (id) => {
  return api.get('/offers/offers/'+id)
}

export const createOffer = (data) => {
  return api.post('/offers/offers', data)
};

export const updateProductsByOfferId = (id, data) => {
  return api.post('/offers/offers/'+id+'/products', data)
};

export const updateOfferById = (id, data) => {
  return api.put('/offers/offers/'+id, data)
};

export const getExtendOffer = (id) => {
  return api.get('/offers/offers/'+id+'/extendInfo')
};

export const updateStatusOffer = (id, data) => {
  return api.put('/offers/offers/'+id+'/status', data)
};

export const deleteOfferById = (id) => {
  return api.delete('/offers/offers/'+id)
}

export const getAllClients = () => {
  return api.get('/offers/clients');
};

export const getClientById = (id) => {
  return api.get('/offers/clients/'+id);
}

export const updateClientById = (id, data) => {
  return api.put('/offers/clients/'+id, data)
};

export const createClient = (data) => {
  return api.post('/offers/clients', data)
};

export const deleteClientById = (id) => {
    return api.delete('/offers/clients/'+id)
}

export const deleteProductById = (id) => {
  return api.delete('/offers/products/'+id)
}

export const getAllProducts = () => {
  return api.get('/offers/products');
};

export const getProductById = (id) => {
  return api.get('/offers/products/'+id);
}

export const updateProductById = (id, data) => {
  return api.put('/offers/products/'+id, data)
};

export const createProduct = (data) => {
  return api.post('/offers/products', data)
};

export const getAllCategories = () => {
  return api.get('/offers/category');
};

export const deleteCategoryById = (id) => {
  return api.delete('/offers/category/'+id);
}

export const getCategoryById = (id) => {
  return api.get('/offers/category/'+id);
}

export const updateCategoryById = (id, data) => {
  return api.put('/offers/category/'+id, data)
};

export const createCategory = (data) => {
  return api.post('/offers/category', data)
};

export const getAllUsers = () => {
  return api.get('/users/users');
};

export const createUser = (data) => {
  return api.post('/users/users', data)
};

export const getUserById = (id) => {
  return api.get('/users/users/'+id);
}

export const updateUserById = (id, data) => {
  return api.put('/users/users/'+id, data)
};

export const deleteUserById = (id) => {
  return api.delete('/users/users/'+id)
}

export const printOfferById = (id) => {
  return api.get('/offers/offers/'+id+'/pdf', {
    responseType: 'arraybuffer',
  });
}

export const sendOfferApi = (id) => {
  return api.post('/offers/offers/'+id+'/sendoffer')
};

export const getNotifications = () => {
  return api.get('/notifications/notifications');
}

export const getConfig = () => {
  return api.get('/notifications/config');
}

export const updateConfig = (data) => {
  return api.put('/notifications/config', data)
};

export const testMail = () => {
  return api.post('/notifications/config/testMail')
};
