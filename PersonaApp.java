/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.personaapp;

/**
 *
 * @author adr
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;


class Persona implements Serializable {
    public String id;
    public String nombre;
    public int edad;
    public String genero;
    

    public Persona(String id, String nombre, int edad, String genero) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.genero = genero;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public String getGenero() {
        return genero;
    }

    @Override
    public String toString() {
        return id + ", " + nombre + ", " + edad + ", " + genero;
    }
}

public class PersonaApp extends JFrame {
    private JTextArea textArea;
    private File file = new File("personas.dat");

    public PersonaApp() {
        setTitle("Gestor de Personas");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(1, 4));

        // Botón Crear
        JButton btnCrear = new JButton("Crear");
        btnCrear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirFormulario();
            }
        });
        panelPrincipal.add(btnCrear);

        // Botón Leer
        JButton btnLeer = new JButton("Leer");
        btnLeer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                leerArchivo();
            }
        });
        panelPrincipal.add(btnLeer);

        // Botón Modificar
        JButton btnModificar = new JButton("Modificar");
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modificarRegistro();
            }
        });
        panelPrincipal.add(btnModificar);

        // Botón Borrar
        JButton btnBorrar = new JButton("Borrar");
        btnBorrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borrarRegistro();
            }
        });
        panelPrincipal.add(btnBorrar);

        add(panelPrincipal, BorderLayout.NORTH);

        // Área de texto para mostrar el contenido
        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    // Formulario para capturar los datos de la persona
    private void abrirFormulario() {
        JFrame formFrame = new JFrame("Formulario");
        formFrame.setSize(300, 250);
        formFrame.setLayout(new GridLayout(5, 2));

        JLabel labelId = new JLabel("ID:");
        JTextField textId = new JTextField();

        JLabel labelNombre = new JLabel("Nombre:");
        JTextField textNombre = new JTextField();

        JLabel labelEdad = new JLabel("Edad:");
        JTextField textEdad = new JTextField();

        JLabel labelGenero = new JLabel("Género:");
        JTextField textGenero = new JTextField();

        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarRegistro(new Persona(textId.getText(), textNombre.getText(), 
                                            Integer.parseInt(textEdad.getText()), textGenero.getText()));
                formFrame.dispose();
            }
        });

        formFrame.add(labelId);
        formFrame.add(textId);
        formFrame.add(labelNombre);
        formFrame.add(textNombre);
        formFrame.add(labelEdad);
        formFrame.add(textEdad);
        formFrame.add(labelGenero);
        formFrame.add(textGenero);
        formFrame.add(new JLabel()); // Espacio vacío
        formFrame.add(btnEnviar);

        formFrame.setVisible(true);
    }

    
    // Clase auxiliar para evitar escribir el encabezado repetidamente
class AppendableObjectOutputStream extends ObjectOutputStream {
    public AppendableObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        // No escribir el encabezado de nuevo en el archivo existente
        reset();
    }
}

// Método para guardar registro de Persona
private void guardarRegistro(Persona persona) {
    try {
        ObjectOutputStream oos;
        if (file.exists() && file.length() > 0) {
            // Si el archivo existe y tiene contenido, usa AppendableObjectOutputStream
            oos = new AppendableObjectOutputStream(new FileOutputStream(file, true));
        } else {
            // Si el archivo no existe o está vacío, usa un ObjectOutputStream normal
            oos = new ObjectOutputStream(new FileOutputStream(file));
        }

        oos.writeObject(persona);
        oos.close();

        JOptionPane.showMessageDialog(this, "Registro guardado correctamente.");
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    // Leer el archivo y mostrar en el JTextArea
    private void leerArchivo() {
        textArea.setText("");  // Limpiar área de texto
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                Persona persona = (Persona) ois.readObject();
                textArea.append(persona + "\n");
            }
        } catch (EOFException e) {
            // Fin del archivo alcanzado
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Modificar un registro
    private void modificarRegistro() {
        String id = JOptionPane.showInputDialog("Ingrese el ID del registro a modificar:");
        if (id == null || id.isEmpty()) return;

         boolean encontrado = false;
         List<Persona> personas = new ArrayList<>();
                
        try {
            //List<Persona> personas = new ArrayList<>();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        //    boolean encontrado = false;
            Persona persona;

            while (true) {
                persona = (Persona) ois.readObject();
                if (persona.getId().equals(id)) {
                    String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre:", persona.getNombre());
                    String nuevoEdad = JOptionPane.showInputDialog("Nueva edad:", persona.getEdad());
                    String nuevoGenero = JOptionPane.showInputDialog("Nuevo género:", persona.getGenero());
                    personas.add(new Persona(id, nuevoNombre, Integer.parseInt(nuevoEdad), nuevoGenero));
                    encontrado = true;
                } else {
                    personas.add(persona);
                }
            }
        } catch (EOFException e) {
            // Fin del archivo alcanzado
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
 //       boolean encontrado;

        if (encontrado) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
   //             Iterable<Persona> personas;
                for (Persona p : personas) {
                    oos.writeObject(p);
                }
                JOptionPane.showMessageDialog(this, "Registro modificado.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Registro no encontrado.");
        }
    }

    // Borrar un registro por ID
    private void borrarRegistro() {
        String id = JOptionPane.showInputDialog("Ingrese el ID del registro a borrar:");
        if (id == null || id.isEmpty()) return;

        boolean encontrado = false;
        List<Persona> personas = new ArrayList<>();
        
        try {
//            List<Persona> personas = new ArrayList<>();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
           // boolean encontrado = false;
            Persona persona;

            while (true) {
                persona = (Persona) ois.readObject();
                if (!persona.getId().equals(id)) {
                    personas.add(persona);
                } else {
                    encontrado = true; // Registro encontrado y no agregado a la lista
                }
            }
        } catch (EOFException e) {
            // Fin del archivo alcanzado
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //boolean encontrado;
        

        if (encontrado) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                //Iterable<Persona> personas;
                for (Persona p : personas) {
                    oos.writeObject(p);
                }
                JOptionPane.showMessageDialog(this, "Registro borrado.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Registro no encontrado.");
        }
    }
    
 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PersonaApp app = new PersonaApp();
                app.setVisible(true);
            }
        });
    }
}
